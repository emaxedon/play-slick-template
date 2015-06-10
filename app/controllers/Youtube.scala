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

object Youtube extends Controller with Secured {

	val config = ConfigFactory.load

	val key = config.getString("youtube.apiKey")

	def search(username: String) = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		Await.result(
			WS.url("https://www.googleapis.com/youtube/v3/channels")
				.withQueryString(
					"part" -> "contentDetails", 
					"forUsername" -> username, 
					"maxResults" -> "1",
					"fields" -> "items",
					"key" -> key).get().map { response =>
						Ok(toJson(response.json))
					}
		, Duration(10000, MILLISECONDS))
	}

}