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
	val key = config.getString("smtp.password")

	def send = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		val json: JsValue = Json.obj(
			"key" -> key,
			"text" -> "Example email",
			"subject" -> "example subject",
			"from_email" -> "emaxedon@gmail.com",
			"to" -> Json.arr(Json.obj("email" -> "emaxedon@gmail.com"))
		)

		Ok("")

		// Await.result(WS.url("https://api.twitter.com/1.1/users/search.json")
		// 	.withQueryString("q" -> screenName)
		// 	.sign(OAuthCalculator(key, credentials)).get().map { response =>
		// 		Ok(toJson(response.json))
		// 	}, Duration(10000, MILLISECONDS))
	}

}