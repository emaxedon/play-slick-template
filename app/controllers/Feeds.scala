package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.libs.json.Json._
import play.api.libs.json._
import play.api.Play.current
import play.api.Logger
import java.io.File
import models._
import services._
import helpers._

object Feeds extends Controller with Secured {

	private def feedJson(feed: Feed) = toJson(new FeedJson(feed, FeedService.getChildren(feed.id.get)))
	private def feedsJson(feeds: Seq[Feed]) = Json.obj("feeds" -> feeds.map(feedJson _))

	private def userTimelineJson(feeds: Seq[Feed], page: Int, pageSize: Int) = {
		Json.obj("timeline" -> DataService.listPage(feeds.map(_.id.get), page, pageSize).map(new DataJson(_)))
	}

	private def timelineJson(feedId: Int, page: Int, pageSize: Int) = {
		Json.obj("timeline" -> DataService.listPage(feedId, page, pageSize).map(new DataJson(_)))
	}

	def create = IsAdministrator(parse.json) { implicit user => implicit request =>
		request.body.validate[FeedDetails].map { details =>
			geocode( details.location ) match {
				case Some( location ) =>
					FeedService.create(details.name, details.category, details.facebookApi, details.twitterApi, details.instagramApi,
						details.youtubeApi, details.location, location) match {
							case Some(feed) => Ok(resultJson(1, "created feed", feedJson(feed)))
							case None => Ok(resultJson(0, "Oops! Problem creating feed.", JsNull))
					}
				case None =>
					Ok(resultJson(0, "Oops! Unrecognized location.", JsNull))
			}
		}.getOrElse(Ok(resultJson(0, "Oops! Invalid json.", JsNull)))
	}
	
	def all = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		Ok(resultJson(1, "all feeds", feedsJson(FeedService.list)))
	}
	
	def list = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		Ok(resultJson(1, "user feeds", feedsJson(FeedService.findByUser(user.id.get))))
	}

	def popular = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		Ok(resultJson(1, "popular feeds", feedsJson(FeedService.listPopular)))
	}

	def addPopular(feedId: Int) = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		FeedService.addPopular(feedId)

		Ok(resultJson(1, "feed has been made popular", JsNull))
	}

	def removePopular(feedId: Int) = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		FeedService.removePopular(feedId) match {
			case true => Ok(resultJson(1, "feed has been removed from popular feeds", JsNull))
			case false => Ok(resultJson(0, "Oops! Something went wrong.", JsNull))
		}
	}

	def trending = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		Ok(resultJson(1, "trending feeds", feedsJson(FeedService.listTrending)))
	}

	def addTrending(feedId: Int) = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		FeedService.addTrending(feedId)

		Ok(resultJson(1, "feed has been made trending", JsNull))
	}

	def removeTrending(feedId: Int) = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		FeedService.removeTrending(feedId) match {
			case true => Ok(resultJson(1, "feed has been removed from trending feeds", JsNull))
			case false => Ok(resultJson(0, "Oops! Something went wrong.", JsNull))
		}
	}

	def userTimeline(page: Int, pageSize: Int) = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		val feeds = FeedService.find(UserService.following(user.id.get))

		Ok(resultJson(1, "user feeds timeline", userTimelineJson(feeds, page, pageSize)))
	}

	def timeline(id: Int, page: Int, pageSize: Int) = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		Ok(resultJson(1, "feed timeline", timelineJson(id, page, pageSize)))
	}

	def find(feedId: Int) = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		FeedService.find(feedId) match {
			case Some( f ) => Ok(resultJson(1, "feed by id", feedJson(f)))
			case None => Ok(resultJson(0, "Oops! Non-existent feed.", JsNull))
		}
	}

	def update(id: Int) = IsAdministrator(parse.json) { implicit user => implicit request =>
		request.body.validate[FeedDetails].map { feedDetails =>
			if (FeedService.update( id, feedDetails ) != None)
				Ok(resultJson(1, "feed updated", JsNull))
			else
				Ok(resultJson(0, "Oops! Problem updating feed.", JsNull))
		}.getOrElse(Ok(resultJson(0, "Oops! Invalid json.", JsNull)))
	}

	def recent(size: Int) = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		Ok(resultJson(1, "recent feeds", feedsJson(FeedService.recent(size))))
	}
	
	def count = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		Ok(resultJson(1, "feed count", Json.obj("count" -> FeedService.count)))
	}

	def search(q: String) = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		Ok(resultJson(1, "feeds", feedsJson(FeedService.prefix(q))))
	}

	def remove(id: Int) = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		if (FeedService.remove( id ) == 1)
			Ok(resultJson(1, "removed feed", JsNull))
		else
			Ok(resultJson(0, "Oops! Problem removing feed.", JsNull))
	}
	
	def nearby(radius: Double) = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		if (radius > 0 && radius <= 200)
			Ok(resultJson(1, "nearby feeds", feedsJson(FeedService.nearby(Geo(user.latitude, user.longitude), radius))))
		else
			Ok(resultJson(0, "Oops! search radius out of range (0 < radius <= 200).", JsNull))
	}

	def follow(feedId: Int) = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		UserService.follow(user.id.get, feedId)

		Ok(resultJson(1, "successfully following feed", JsNull))
	}

	def unfollow(feedId: Int) = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		UserService.unfollow(user.id.get, feedId) match {
			case true => Ok(resultJson(1, "successfully unfollowing feed", JsNull))
			case false => Ok(resultJson(0, "failed to unfollow feed", JsNull))
		}
	}

	def upload = IsAdministrator(parse.multipartFormData) { implicit user => implicit request =>
		request.body.file("file").map { file =>
			val contentType = file.contentType
			val fileName = randomString(10)
			
			file.ref.moveTo(new File("/tmp/" + fileName))

			FeedService.importCSV(new File("/tmp/" + fileName))

			Ok(resultJson(1, "successfully imported feeds", JsNull))
		}.getOrElse {
			Logger.debug("oops")
			Ok(resultJson(0, "Oops! An error occured.", JsNull))
		}
	}

	def download = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		val file = FeedService.exportCSV
		
		Ok.sendFile(file)
	}
	
}