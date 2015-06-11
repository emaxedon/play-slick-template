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

object Facebook extends Controller with Secured {

	val config = ConfigFactory.load
	val accessToken = config.getString("facebook.accessToken")

	def search(pageName: String) = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		Await.result(WS.url("https://graph.facebook.com/search")
			.withQueryString("q" -> pageName, "type" -> "page", "access_token" -> accessToken).get().map { response =>
				Ok(toJson(response.json))
			}, Duration(10000, MILLISECONDS))
	}

}