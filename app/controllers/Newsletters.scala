package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json._
import play.api.libs.json._
import play.api.Play.current
import com.typesafe.config._
import play.api.libs.ws._
import play.api.libs.oauth._
import scala.concurrent.ExecutionContext.Implicits.global
import models._
import services._
import helpers._
import scala.concurrent.Await
import scala.concurrent.duration._

object Newsletters extends Controller with Secured {

	val config = ConfigFactory.load
	val mandrillKey = config.getString("smtp.password")

	def send(id: Int) = IsAdministrator(parse.json) { implicit user => implicit request =>
		request.body.validate[NewsletterDetails].map { details =>

			val email = Json.obj(
				"key" -> mandrillKey,
				"message" -> Json.obj(
					"html" -> details.text,
					"subject" -> details.subject,
					"from_email" -> "emaxedon@gmail.com",
					"to" -> UserService.list.map(u => Json.obj("email" -> u.email))
				)
			)

			Await.result(WS.url("https://mandrillapp.com/api/1.0/messages/send.json").post(email).map { response =>
					Ok(toJson(response.json))
				}, Duration(10000, MILLISECONDS))

			NewsletterService.update( id, Some(details.subject), Some(details.text), Some(1) )

			NewsletterService.find(id) match {
				case Some(newsletter) => Ok(toJson(new NewsletterJson(newsletter)))
				case None => Ok(JsNull)
			}
		}.getOrElse(Ok(JsNull))
	}

	def create = IsAdministrator(parse.json) { implicit user => implicit request =>
		request.body.validate[NewsletterDetails].map { details =>
			NewsletterService.create(details.subject, details.text) match {
					case Some(newsletter) => Ok(toJson(new NewsletterJson(newsletter)))
					case None => Ok(JsNull)
			}
		}.getOrElse(Ok(JsNull))
	}

	def update(id: Int) = IsAdministrator(parse.json) { implicit user => implicit request =>
		request.body.validate[NewsletterUpdate].map { details =>
			if (NewsletterService.update( id, details.subject, details.text, details.status ) > 0)
				Ok(JsNull)
			else
				Ok(JsNull)
		}.getOrElse(Ok(JsNull))
	}

	def list = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		Ok(toJson(NewsletterService.list.map(new NewsletterJson(_))))
	}

	def find(id: Int) = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		NewsletterService.find(id) match {
			case Some( n ) => Ok(toJson(new NewsletterJson(n)))
			case None => Ok(JsNull)
		}
	}

	def remove(id: Int) = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		if (NewsletterService.remove( id ) > 0)
			Ok(JsNull)
		else
			Ok(JsNull)
	}

}