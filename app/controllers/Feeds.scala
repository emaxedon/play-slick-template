package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.libs.json.Json._
import play.api.libs.json._
import play.api.Play.current
import play.api.Logger
import models._
import services._
import helpers._

object Feeds extends Controller with Secured {

	private def feedDataJson(feed: Feed) = toJson(new FeedJson(feed, FeedService.getChildren(feed.id.get), DataService.list(feed.id.get).map(new DataJson(_))))
	private def feedJson(feed: Feed) = toJson(new FeedJson(feed, FeedService.getChildren(feed.id.get), Seq.empty[DataJson]))
	private def feedsJson(feeds: Seq[Feed]) = Json.obj("feeds" -> feeds.map(feedJson _))

	def create = IsAdministrator(parse.json) { implicit user => implicit request =>
		request.body.validate[FeedDetails].map { details =>
			geocode( details.location ) match {
				case Some( location ) =>
					FeedService.create(details.name, details.category, details.facebookApi, details.twitterApi, details.instagramApi,
						details.youtubeApi, details.location, location) match {
							case Some(feed) => Ok(resultJson(0, "created feed", feedJson(feed)))
							case None => Ok(resultJson(3, "Oops! Problem creating feed.", JsNull))
					}
				case None =>
					Ok(resultJson(2, "Oops! Unrecognized location.", JsNull))
			}
		}.getOrElse(Ok(resultJson(1, "Oops! Invalid json.", JsNull)))
	}
	
	def all = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		Ok(resultJson(0, "all feeds", feedsJson(FeedService.list)))
	}
	
	def list = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		Ok(resultJson(0, "user feeds", feedsJson(FeedService.findByUser(user.id.get))))
	}

	def find(feedId: Int) = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		FeedService.find(feedId) match {
			case Some( f ) => Ok(resultJson(0, "feed by id", feedDataJson(f)))
			case None => Ok(resultJson(1, "Oops! Non-existent feed.", JsNull))
		}
	}

	def update(id: Int) = IsAdministrator(parse.json) { implicit user => implicit request =>
		request.body.validate[FeedDetails].map { feedDetails =>
			if (FeedService.update( id, feedDetails ) != None)
				Ok(resultJson(0, "feed updated", JsNull))
			else
				Ok(resultJson(2, "Oops! Problem updating feed.", JsNull))
		}.getOrElse(Ok(resultJson(1, "Oops! Invalid json.", JsNull)))
	}

	def recent(size: Int) = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		Ok(resultJson(0, "recent feeds", feedsJson(FeedService.recent(size))))
	}
	
	def count = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		Ok(resultJson(0, "feed count", Json.obj("count" -> FeedService.count)))
	}

	def prefix = IsAuthenticated(parse.json) { implicit user => implicit request =>
		(request.body \ "search").asOpt[String].map { search =>
			Ok(resultJson(0, "feeds", feedsJson(FeedService.prefix(search))))
		}.getOrElse(Ok(resultJson(1, "Oops! Invalid json.", JsNull)))
	}

	def remove(id: Int) = IsAdministrator(parse.anyContent) { implicit user => implicit request =>
		if (FeedService.remove( id ) == 1)
			Ok(resultJson(0, "removed feed", JsNull))
		else
			Ok(resultJson(1, "Oops! Problem removing feed.", JsNull))
	}
	
	def nearby(radius: Double) = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		if (radius > 0 && radius <= 200)
			Ok(resultJson(0, "nearby feeds", feedsJson(FeedService.nearby(Geo(user.latitude, user.longitude), radius))))
		else
			Ok(resultJson(1, "Oops! search radius out of range (0 < radius <= 200).", JsNull))
	}

	def follow(feedId: Int) = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		UserService.follow(user.id.get, feedId)

		Ok(resultJson(0, "successfully following feed", JsNull))
	}

	def unfollow(feedId: Int) = IsAuthenticated(parse.anyContent) { implicit user => implicit request =>
		UserService.unfollow(user.id.get, feedId) match {
			case true => Ok(resultJson(0, "successfully unfollowing feed", JsNull))
			case false => Ok(resultJson(1, "failed to unfollow feed", JsNull))
		}
	}
	
}