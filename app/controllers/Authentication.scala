package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.libs.json.Json._
import play.api.libs.json._
import play.api.libs.mailer._
import play.api.Play.current
import play.api.Logger
import models._
import services._
import helpers._

object Authentication extends Controller with Secured {

	def login = Action(parse.json) { implicit request =>
		request.body.validate[UserAuth].map { userAuth =>
			UserService.authenticate(userAuth.email, userAuth.password) match {
				case Some(user) =>
					val userJson = new UserJson(user)
					Ok(resultJson(0, "Success! You are now logged in.", toJson(userJson))).withSession("user_id" -> user.id.get.toString)
				case None => Ok(resultJson(1, "Oops! User email or password incorrect.", JsNull))
			}
		}.getOrElse(Ok(resultJson(1, "Oops! Invalid json.", JsNull)))
	}

	def loginOnce(userId: Int, passwordToken: String) = Action { implicit request =>
		UserService.authenticateOnce(userId, passwordToken) match {
			case Some(user) =>
				val userJson = new UserJson(user)
				Ok(resultJson(0, "Success! You are now logged in.", toJson(userJson))).withSession("user_id" -> user.id.get.toString)
			case None => Ok(resultJson(1, "Oops! Reset password token invalid.", JsNull))
		}
	}

	def register = Action(parse.json) { implicit request =>
		request.body.validate[UserDetails].map { details =>
			geocode( details.location ) match {
				case Some( location ) =>
					UserService.create(details.email, details.password, "user", details.location, location) match {
						case Some(user) =>
							val userJson = new UserJson(user)
							Ok(resultJson(0, "registered", toJson(userJson))).withSession("user_id" -> user.id.get.toString)
						case None =>
							Ok(resultJson(3, "Oops! Problem registering user.", JsNull))
						}
				case None =>
					Ok(resultJson(2, "Oops! Unrecognized postal code.", JsNull))
			}
		}.getOrElse(Ok(resultJson(1, "Oops! Invalid json.", JsNull)))
	}

	def create = IsAdministrator(parse.json) { implicit user => implicit request =>
		request.body.validate[UserDetails].map { details =>
			geocode( details.location ) match {
				case Some( location ) =>
					UserService.create(details.email, details.password, details.role.get, details.location, location) match {
						case Some(user) =>
							val userJson = new UserJson(user)
							Ok(resultJson(0, "registered", toJson(userJson)))
						case None =>
							Ok(resultJson(3, "Oops! Problem registering user.", JsNull))
						}
				case None =>
					Ok(resultJson(2, "Oops! Unrecognized location.", JsNull))
			}
		}.getOrElse(Ok(resultJson(1, "Oops! Invalid json.", JsNull)))
	}

	def logout = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		Ok(resultJson(0, "Success! You have been logged out.", JsNull)).withNewSession
	}

	def user = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		val userJson = new UserJson(user)
		Ok(resultJson(0, "Success! You have retrieved user data.", toJson(userJson)))
	}

	def users = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		Ok(resultJson(0, "Success! You have retrieved users", Json.obj("users" -> UserService.list.map(user => new UserJson(user)))))
	}

	def recent(size: Int) = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		Ok(resultJson(0, "Success! You have retrieved users", Json.obj("users" -> UserService.recent(size).map(user => new UserJson(user)))))
	}

	def prefix = IsAdministrator(parse.json) { implicit user => implicit request =>
		(request.body \ "search").asOpt[String].map { search =>
			Ok(resultJson(0, "Success! You have retrieved users", Json.obj("users" -> UserService.prefix(search).map(user => new UserJson(user)))))
		}.getOrElse(Ok(resultJson(1, "Oops! Invalid json.", JsNull)))
	}

	def prefixCount = IsAdministrator(parse.json) { implicit user => implicit request =>
		(request.body \ "search").asOpt[String].map { search =>
			Ok(resultJson(0, "user count", Json.obj("count" -> UserService.prefixCount(search))))
		}.getOrElse(Ok(resultJson(1, "Oops! Invalid json.", JsNull)))
	}

	def count = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		Ok(resultJson(0, "user count", Json.obj("count" -> UserService.count)))
	}

	def password = IsAuthenticated(parse.json) { implicit user => implicit request =>
		(request.body \ "password").asOpt[String].map { password =>
			UserService.changePassword( user.email, password ) match {
				case Some(user) =>
					val userJson = new UserJson(user)
					Ok(resultJson(0, "Success! Password has been updated.", toJson(userJson)))
				case None => Ok(resultJson(1, "Oops! Failed to update password.", JsNull))
			}
		}.getOrElse(Ok(resultJson(1, "Oops! Invalid json.", JsNull)))
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
					MailerPlugin.send(email)

					Ok(resultJson(0, "Success! Password has been reset and password token has been sent to your email.", JsNull))
				case None => Ok(resultJson(1, "Oops! Failed to reset password.", JsNull))
			}
		}.getOrElse(Ok(resultJson(1, "Oops! Invalid json.", JsNull)))
	}
	
	def remove(id: Int) = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		if (UserService.remove( id ) == 1)
			Ok(resultJson(0, "user removed", JsNull))
		else
			Ok(resultJson(1, "Oops! Problem removing user.", JsNull))
	}

	def find(id: Int) = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		UserService.find( id ) match {
			case Some( u ) =>
				Ok(resultJson(0, "user details", toJson(new UserJson(u))))
			case None =>
				Ok(resultJson(1, "Oops! Non-existent user.", JsNull))
		}
	}

	def update(id: Int) = IsAdministrator(parse.json) { implicit user => implicit request =>
		request.body.validate[UserDetails].map { userDetails =>
			if (UserService.update( id, userDetails ) == 1)
				Ok(resultJson(0, "user updated", JsNull))
			else
				Ok(resultJson(1, "Oops! Problem updating user.", JsNull))
		}.getOrElse(Ok(resultJson(1, "Oops! Invalid json.", JsNull)))
	}
}

/**
 * Provide security features
 */
trait Secured {

	def resultJson(result: Int, message: String, data: JsValue): JsValue = Json.obj("result" -> result, "message" -> message, "data" -> data)

	/**
	 * Retrieve the connected user ID.
	 */
	private def userId(request: RequestHeader): Option[String] = request.session.get("user_id")

	/**
	 * Return failed message.
	 */
	private def onUnauthorized(request: RequestHeader) = Ok(resultJson(1, "unauthorized attempt", JsNull))

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
				case None => Ok(resultJson(1, "Oops! User is unauthorized.", JsNull))
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
					else Ok(resultJson(1, "Oops! You are not an administrator.", JsNull))
				case None => Ok(resultJson(1, "Oops! User is unauthorized.", JsNull))
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
