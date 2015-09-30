package controllers

import java.io.File
import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.libs.json.Json._
import play.api.libs.json._
import play.api.libs.mailer._
import play.api.Play.current
import play.api.libs.ws._
import play.api.Logger
import com.typesafe.config._
import models._
import services._
import helpers._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._

object Authentication extends Controller with Secured {

	val config = ConfigFactory.load

	val mailchimpKey = config.getString("mailchimp.apiKey")

	def login = Action(parse.json) { implicit request =>
		request.body.validate[UserAuth].map { userAuth =>
			UserService.authenticate(userAuth.email, userAuth.password) match {
				case Some(user) =>
					val userJson = new UserJson(user)
					Ok(toJson(userJson)).withSession("user_id" -> user.id.get.toString)
				case None => Ok(JsNull)
			}
		}.getOrElse(Ok(JsNull))
	}

	def loginOnce(userId: Int, passwordToken: String) = Action { implicit request =>
		UserService.authenticateOnce(userId, passwordToken) match {
			case Some(user) =>
				val userJson = new UserJson(user)
				Ok(views.html.reset("Reset password")).withSession("user_id" -> user.id.get.toString)
			case None => Ok(resultJson(0, "Oops! Reset password token invalid.", JsNull))
		}
	}

	def register = Action(parse.json) { implicit request =>
		request.body.validate[UserDetails].map { details =>
			geocode( details.location ) match {
				case Some( location ) =>
					UserService.create(details.email, details.password, "user", details.location, location) match {
						case Some(user) =>
							val userJson = new UserJson(user)

							WS.url("https://us11.api.mailchimp.com/3.0/lists/c46ee56979/members")
								.withAuth("emaxedon", mailchimpKey, WSAuthScheme.BASIC)
								.post(toJson(Json.obj("email_address" -> user.email, "status" -> "subscribed")))

							Ok(toJson(userJson)).withSession("user_id" -> user.id.get.toString)
						case None =>
							Ok(JsNull)
						}
				case None =>
					Ok(JsNull)
			}
		}.getOrElse(Ok(JsNull))
	}

	def create = IsAdministrator(parse.json) { implicit user => implicit request =>
		request.body.validate[UserDetails].map { details =>
			geocode( details.location ) match {
				case Some( location ) =>
					UserService.create(details.email, details.password, details.role.get, details.location, location) match {
						case Some(user) =>
							val userJson = new UserJson(user)
							Ok(toJson(userJson))
						case None =>
							Ok(JsNull)
						}
				case None =>
					Ok(JsNull)
			}
		}.getOrElse(Ok(JsNull))
	}

	def logout = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		Ok(JsNull).withNewSession
	}

	def user = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		val userJson = new UserJson(user)
		Ok(toJson(userJson))
	}

	def users = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		Ok(Json.obj("users" -> UserService.list.map(user => new UserJson(user))))
	}

	def recent(size: Int) = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		Ok(Json.obj("users" -> UserService.recent(size).map(user => new UserJson(user))))
	}

	def search(q: String) = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		Ok(Json.obj("users" -> UserService.prefix(q).map(user => new UserJson(user))))
	}

	def prefixCount = IsAdministrator(parse.json) { implicit user => implicit request =>
		(request.body \ "search").asOpt[String].map { search =>
			Ok(Json.obj("count" -> UserService.prefixCount(search)))
		}.getOrElse(Ok(JsNull))
	}

	def count = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		Ok(Json.obj("count" -> UserService.count))
	}

	def password = IsAuthenticated(parse.json) { implicit user => implicit request =>
		(request.body \ "password").asOpt[String].map { password =>
			UserService.changePassword( user.email, password ) match {
				case Some(user) =>
					val userJson = new UserJson(user)
					Ok(toJson(userJson))
				case None => Ok(JsNull)
			}
		}.getOrElse(Ok(JsNull))
	}

	def forgotPassword = Action(parse.json) { implicit request =>
		(request.body \ "email").asOpt[String].map { userEmail =>
			UserService.resetPassword(userEmail) match {
				case Some(resetPassword) =>
					val email = Email(
						"Password Reset",
						"<emaxedon@gmail.com>",
						Seq(userEmail),
						bodyText = Some("Use the following link to login and then change your password: http://" + request.host + routes.Authentication.loginOnce(resetPassword.userId, resetPassword.passwordToken).toString)
					)
					try {
						MailerPlugin.send(email)

						Ok(JsNull)
					} catch {
						case e: Exception =>
							val mandrillKey = config.getString("smtp.password")

							val emailJson = Json.obj(
								"key" -> mandrillKey,
								"message" -> Json.obj(
									"text" -> ("Use the following link to login and then change your password: http://" + request.host + routes.Authentication.loginOnce(resetPassword.userId, resetPassword.passwordToken).toString),
									"subject" -> "Password Reset",
									"from_email" -> "emaxedon@gmail.com",
									"to" -> Json.arr(Json.obj("email" -> userEmail))
								)
							)

							Await.result(WS.url("https://mandrillapp.com/api/1.0/messages/send.json").post(emailJson).map { response =>
								Ok(JsNull)
							}, Duration(5000, MILLISECONDS))
					}
				case None => Ok(JsNull)
			}
		}.getOrElse(Ok(JsNull))
	}
	
	def remove(id: Int) = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		if (UserService.remove( id ) == 1)
			Ok(JsNull)
		else
			Ok(JsNull)
	}

	def find(id: Int) = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		UserService.find( id ) match {
			case Some( u ) =>
				Ok(toJson(new UserJson(u)))
			case None =>
				Ok(JsNull)
		}
	}

	def update(id: Int) = IsAdministrator(parse.json) { implicit user => implicit request =>
		request.body.validate[UserUpdate].map { userUpdate =>
			val someInt = UserService.update( id, userUpdate )

			Logger.debug(someInt.toString)
			
			if (someInt == 1)
				Ok(JsNull)
			else
				Ok(JsNull)
		}.getOrElse(JsNull))
	}
}

/**
 * Provide security features
 */
trait Secured {

	/**
	 * Retrieve the connected user ID.
	 */
	private def userId(request: RequestHeader): Option[String] = request.session.get("user_id")

	/**
	 * Return failed message.
	 */
	private def onUnauthorized(request: RequestHeader) = Ok(resultJson(0, "unauthorized attempt", JsNull))

	def CheckIfAuthenticated[A](userinfo: RequestHeader => Option[A])(action: Option[A] => EssentialAction): EssentialAction = {
		EssentialAction { request =>
			userinfo(request).map { user =>
				action(Some(user))(request)
			}.getOrElse( action(None)(request) )
		}
	}

	/**
	 * Action for authenticated users.
	 */
	def IsAuthenticated[A](bodyParser: BodyParser[A])(f: => User => Request[A] => Result) = Security.Authenticated(userId, onUnauthorized) { userId =>
		Action(bodyParser) { request =>
			UserService.find(userId.toInt) match {
				case Some(user) => f(user)(request)
				case None => Ok(JsNull)
			}
		}
	}

	/**
	 * Action for administrators.
	 */
	def IsAdministrator[A](bodyParser: BodyParser[A])(f: => User => Request[A] => Result) = Security.Authenticated(userId, onUnauthorized) { userId =>
		Action(bodyParser) { request =>
			UserService.find(userId.toInt) match {
				case Some(user) =>
					if (user.role == "admin") f(user)(request)
					else Ok(JsNull)
				case None => Ok(JsNull)
			}
		}
	}

	/**
	 * Action for possibly authenticated users.
	 */
	def MaybeAuthenticated[A](bodyParser: BodyParser[A])(f: => Option[User] => Request[A] => Result) = CheckIfAuthenticated(userId) { maybeUserId =>
		Action(bodyParser) { request =>
			maybeUserId match {
				case Some(userId) => 
					UserService.find(userId.toInt) match {
						case Some(user) => f(Some(user))(request)
						case None => f(None)(request)
					}
				case None => f(None)(request)
			}
		}
	}

}
