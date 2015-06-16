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
import helpers._
import scala.concurrent.Await
import scala.concurrent.duration._

object Mandrill extends Controller with Secured {

	val config = ConfigFactory.load
	val mandrillKey = config.getString("smtp.password")

	def send = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		val email = Json.obj(
			"key" -> mandrillKey,
			"message" -> Json.obj(
				"text" -> "Example email",
				"subject" -> "Testing mandrill API",
				"from_email" -> "emaxedon@gmail.com",
				"to" -> Json.arr(Json.obj("email" -> "emaxedon@gmail.com"))
			)
		)

		Await.result(WS.url("https://mandrillapp.com/api/1.0/messages/send.json").post(email).map { response =>
				Ok(toJson(response.json))
			}, Duration(10000, MILLISECONDS))
	}

}