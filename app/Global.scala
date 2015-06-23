import util.matching.Regex
import collection.mutable.ArrayBuffer
import util.Random

import play.api.{GlobalSettings, Application}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current
import play.api.libs.ws._
import play.api.libs.oauth._
import scala.concurrent.Future
import play.api.Logger
import com.typesafe.config._
import play.api.libs.json._
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.text.Normalizer
import play.api.libs.json.Json._
import play.api.libs.json._

import models._
import services._
import helpers._


object Global extends GlobalSettings {
	case class Contact(
		first: String,
		last: String,
		address: String,
		city: String,
		prov: String,
		postal: String,
		country: String,
		phone: String,
		location: Geo)
		
	override def onStart(app: Application) {
		val config = ConfigFactory.load
		val ContactRegex = "(.*) (.*), (.*), (.*), (.*), (.*), (.*), (.*), (.*), (.*)"r
		val contacts = new ArrayBuffer[Contact]
		
		def randomContact = {
			val index = Random.nextInt(contacts.size)
			val result = contacts(index)
		
			contacts.remove( index )
			result
		}

		Logger.info( "initializing... (/admin can't be used until admin user has been added)" )
		
		Logger.info( "loading bogus contact data" )
		
		for (line <- io.Source.fromFile( "bogus-contact-info.csv" ).getLines) {
			val ContactRegex(first, last, address, city, prov, postal, country, phone, lat, lng) = line
			
			contacts += Contact(first, last, address, city, prov, postal, country, phone, Geo(lat.toDouble, lng.toDouble))
		}
		
		Logger.info( "adding users" )
		
		UserService.find(config.getString("admin.email")) match {
			case None =>
				val admin = randomContact
				UserService.create(config.getString("admin.email"), config.getString("admin.password"), "admin", admin.postal, admin.location)
				
				for (_ <- 1 to 10) {
					val user = randomContact
				
					UserService.create( user.first.toLowerCase + "." + user.last.toLowerCase + "@example.com", "asdf",
										"user", user.postal, user.location)
				}
			case _ =>
		}
		
		Logger.info( "adding feeds" )
		
		FeedService.list match {
			case Nil =>
				for (i <- 1 to 10) {
					val player = randomContact
					
					FeedService.create( player.first + " " + player.last,
											"player", None, None, None, None, player.postal, player.location )
						
					val team = randomContact
					
					FeedService.create( team.first + "'s Team", "team", None, None, None, None, team.postal, team.location )
				}
			case _ =>
		}

		if (UserService.following(1) isEmpty)
			UserService.follow( 1, 2 )
		
		Logger.info( "done initializing" )

		// val accessToken = config.getString("facebook.accessToken")

		// for {
		// 	pageid <- WS.url("https://graph.facebook.com/search")
		// 					.withQueryString("q" -> Normalizer.normalize("elonmusk", Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", ""), "type" -> "page", "access_token" -> accessToken).get().map { response =>
		// 						(response.json \\ "id").head.as[String]
		// 					}
		// 	pictureUrl <- WS.url("https://graph.facebook.com/v2.3/" + pageid)
		// 					.withQueryString("access_token" -> accessToken, "fields" -> "picture").get().map { response =>
		// 						(response.json \ "picture" \ "data" \ "url").as[String]
		// 					}
		// } yield {
		// 	Logger.debug(pictureUrl)
		// }

		// val mailchimpKey = config.getString("mailchimp.apiKey")

		// WS.url("https://us11.api.mailchimp.com/3.0/lists/c46ee56979/members")
		// 	.withAuth("emaxedon", mailchimpKey, WSAuthScheme.BASIC)
		// 	.post(toJson(Json.obj("email_address" -> "emaxedon@gmail.com", "status" -> "subscribed")))
	}
}