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

object Twitter extends Controller with Secured {

	val config = ConfigFactory.load
	val key = ConsumerKey(config.getString("twitter.apiKey"), config.getString("twitter.apiSecret"))
	val credentials = RequestToken(config.getString("twitter.accessToken"), config.getString("twitter.accessTokenSecret"))

	def search(screenName: String) = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		Await.result(WS.url("https://api.twitter.com/1.1/users/search.json")
			.withQueryString("q" -> screenName)
			.sign(OAuthCalculator(key, credentials)).get().map { response =>
				Ok(toJson(response.json))
			}, Duration(10000, MILLISECONDS))
	}

}