package services

import scala.concurrent.ExecutionContext.Implicits.global
import scala.slick.driver.PostgresDriver.simple._
import play.api.Play.current
import play.api.libs.ws._
import play.api.libs.oauth._
import scala.concurrent.Future
import play.api.Logger
import play.api.libs.json._
import com.typesafe.config._
import java.sql.Timestamp
import java.text.SimpleDateFormat
import models.{FeedDetails, Feed, Data}
import util._
import helpers._
import java.net.URLEncoder

class FeedTable(tag: Tag) extends Table[Feed](tag, "feeds") {

	def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
	def version = column[Long]("version", O.NotNull)
	def category = column[String]("category", O.NotNull)
	def name = column[String]("name", O.NotNull)
	def facebookApi = column[Option[String]]("facebook_api")
	def twitterApi = column[Option[String]]("twitter_api")
	def instagramApi = column[Option[String]]("instagram_api")
	def youtubeApi = column[Option[String]]("youtube_api")
	def dateCreated = column[Timestamp]("date_created", O.NotNull)
	def dateUpdated = column[Timestamp]("date_updated", O.NotNull)
	def location = column[String]("location", O.NotNull)
	def latitude = column[Double]("latitude", O.NotNull)
	def longitude = column[Double]("longitude", O.NotNull)

	def * = (id.?, version, category, name, facebookApi, twitterApi, instagramApi, youtubeApi, dateCreated, dateUpdated, location, latitude, longitude) <>
		(Feed.tupled, Feed.unapply)
}

class FeedRelateTable(tag: Tag) extends Table[(Int, Int)](tag, "feed_relates") {

	def parentFeedId = column[Int]("parent_feed_id")
	def childFeedId = column[Int]("child_feed_id")

	def pk = primaryKey("feed_relates_pk_a", (parentFeedId, childFeedId))

 	def parentFeed = foreignKey("feed_relates_parent_feed_fk", parentFeedId, TableQuery[FeedTable])(_.id, onDelete=ForeignKeyAction.Cascade)
 	def childFeed = foreignKey("feed_relates_feed_fk", childFeedId, TableQuery[FeedTable])(_.id, onDelete=ForeignKeyAction.Cascade)

	def * = (parentFeedId, childFeedId)
}

object FeedService {

	val config = ConfigFactory.load

	val feeds = TableQuery[FeedTable]

	val userFeeds = TableQuery[UserFeedTable]

	val feedRelates = TableQuery[FeedRelateTable]

	val db = play.api.db.slick.DB

	def list: Seq[Feed] = db.withSession { implicit session =>
		feeds.list
	}
	
	def nearby(geo: Geo, radius: Double): Seq[Feed] = db.withSession { implicit session =>
		val (sw, ne) = geoBoundingBox( geo, radius )
		
		feeds.filter( f => f.latitude >= sw.latitude && f.latitude <= ne.latitude &&
			f.longitude >= sw.longitude && f.longitude <= ne.longitude).list
	}
	
	def find(id: Int): Option[Feed] = db.withSession { implicit session =>
		feeds.filter(_.id === id).list.headOption
	}

	def remove(id: Int) = db.withSession { implicit session => feeds.filter(_.id === id).delete }
	
	def prefix(s: String) = db.withSession { implicit session =>
		feeds.filter(_.name.toLowerCase.startsWith(s.toLowerCase)).list
	}
	
	def prefixCount(s: String) = db.withSession { implicit session =>
		feeds.filter(_.name.toLowerCase.startsWith(s.toLowerCase)).length.run
	}

	def findByUser(userId: Int): Seq[Feed] = db.withSession { implicit session =>
		(feeds join userFeeds.filter(_.userId === userId) on (_.id === _.feedId)).map{ case (f, _) => f }.run
	}
	
	def create(name: String, category: String, facebookApi: Option[String], twitterApi: Option[String],
		instagramApi: Option[String], youtubeApi: Option[String], location: String, geo: Geo): Option[Feed] = db.withSession { implicit session =>
		val feedId = (feeds returning feeds.map(_.id)) +=
			Feed(
				category = category,
				name = name,
				facebookApi = facebookApi,
				twitterApi = twitterApi,
				instagramApi = instagramApi,
				youtubeApi = youtubeApi,
				location = location,
				latitude = geo.latitude,
				longitude = geo.longitude)

		facebookApi match {
			case Some(pageName) =>
				val accessToken = config.getString("facebook.accessToken")

				WS.url("https://graph.facebook.com/search")
					.withQueryString("q" -> URLEncoder.encode(pageName, "UTF-8"), "type" -> "page", "access_token" -> accessToken).get().map { response =>
						val pageid = ((response.json \\ "id").head.as[String])

						WS.url("https://graph.facebook.com/v2.3/" + pageid + "/posts")
							.withQueryString("access_token" -> accessToken).get().map { response =>
								val data = response.json \ "data"

								for (e <- data.as[Seq[JsValue]]) {
									val FACEBOOK_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZ")

									val timestamp = new Timestamp(FACEBOOK_DATE_FORMAT.parse((e \ "created_time").as[String]).getTime)
									val text = (e \ "description").as[String]
									val mediaUrl = (e \ "link").as[String]
									val previewUrl = (e \ "link").as[String]
									val dataType = (e \ "type").as[String]
									
									DataService.create(feedId, "facebook", dataType, mediaUrl, previewUrl, text, timestamp)
								}
							}
					}
			case None =>
		}

		twitterApi match {
			case Some(screenName) =>
				val key = ConsumerKey(config.getString("twitter.apiKey"), config.getString("twitter.apiSecret"))
				val credentials = RequestToken(config.getString("twitter.accessToken"), config.getString("twitter.accessTokenSecret"))

				WS.url("https://api.twitter.com/1.1/statuses/user_timeline.json")
					.withQueryString("screen_name" -> screenName)
					.sign(OAuthCalculator(key, credentials)).get().map { response =>
						val data = response.json

						for (e <- data.as[Seq[JsValue]]) {
							val TWITTER_DATE_FORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy")

							val timestamp = new Timestamp(TWITTER_DATE_FORMAT.parse((e \ "created_at").as[String]).getTime)
							val text = (e \ "text").as[String]
							val dataType =  (e \\ "media_url").headOption match {
								case Some(url) => (e \\ "type").head.as[String]
								case None => "tweet"
							}
							val mediaUrl = (e \\ "media_url").headOption match {
								case Some(url) => (e \\ "media_url").head.as[String]
								case None => ""
							}
							val previewUrl = (e \\ "display_url").headOption match {
								case Some(url) => (e \\ "display_url").head.as[String]
								case None => ""
							}

							DataService.create(feedId, "twitter", dataType, mediaUrl, previewUrl, text, timestamp)
						}
					}
			case None =>
		}

		instagramApi match {
			case Some(username) =>
				WS.url("https://api.instagram.com/v1/users/search")
					.withHeaders("Accept" -> "application/json")
					.withQueryString("client_id" -> config.getString("instagram.clientId"), "q" -> username).get().map { response =>
						val userid = ((response.json \\ "id").head.as[String])

						val userMedia = WS.url("https://api.instagram.com/v1/users/" + userid + "/media/recent")
							.withHeaders("Accept" -> "application/json")
							.withQueryString("client_id" -> config.getString("instagram.clientId")).get().map { response =>
								(response.json \ "meta" \ "code").as[Int] match {
									case 200 =>
										val data = response.json \ "data"
										
										for (e <- data.as[Seq[JsValue]]) {
											val timestamp = new Timestamp((e \ "created_time").as[String].toLong*1000)
											val caption = (e \ "caption" \ "text").as[String]
											val dataType = (e \ "type").as[String]
											val mediaUrl =  (e \ "type").as[String] match {
												case "image" =>
													(e \ "images" \ "standard_resolution" \ "url").as[String]
												case "video" =>
													(e \ "videos" \ "standard_resolution" \ "url").as[String]
												case _ => ""
											}
											val previewUrl = (e \ "images" \ "standard_resolution" \ "url").as[String]
											
											DataService.create(feedId, "instagram", dataType, mediaUrl, previewUrl, caption, timestamp)
										}
									case 400 =>
										Logger.debug((response.json \ "meta" \ "error_message").as[String])
									case _ =>
								}
							}
					}
			case None =>
		}

		youtubeApi match {
			case Some(username) =>
				val key = config.getString("youtube.apiKey")

				WS.url("https://www.googleapis.com/youtube/v3/channels")
					.withQueryString(
						"part" -> "id", 
						"forUsername" -> username,
						"maxResults" -> "1",
						"fields" -> "items",
						"key" -> key).get().map { response =>
							val data = response.json

							val channelId = ((data \ "items").as[Seq[JsValue]].head \ "id").as[String]

							WS.url("https://www.googleapis.com/youtube/v3/search")
								.withQueryString(
									"part" -> "snippet", 
									"channelId" -> channelId,
									"fields" -> "items",
									"maxResults" -> "50",
									"key" -> key).get().map { response =>
										val data = response.json

										for (e <- (data \ "items").as[Seq[JsValue]]) {
											val YOUTUBE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")

											val timestamp = new Timestamp(YOUTUBE_DATE_FORMAT.parse((e \ "snippet" \ "publishedAt").as[String]).getTime)
											val text = (e \ "snippet" \ "description").as[String]
											val mediaUrl = (e \ "id" \ "videoId").as[String]
											val previewUrl = (e \ "snippet" \ "thumbnails" \ "default" \ "url").as[String]
											val dataType = "video"

											DataService.create(feedId, "youtube", dataType, mediaUrl, previewUrl, text, timestamp)
										}
									}
						}
			case None =>
		}

		find(feedId)
	}

	def update(feedId: Int, details: FeedDetails) = db.withSession { implicit session =>
		find(feedId) match {
			case Some(feed) =>
				feeds.filter(_.id === feedId).map(f => (f.version, f.category, f.name, f.facebookApi, f.twitterApi, f.instagramApi, f.youtubeApi, f.dateUpdated)).
					update((feed.version + 1, details.category, details.name, details.facebookApi, details.twitterApi, details.instagramApi, details.youtubeApi, now))
				find(feedId)
			case None => None
		}
	}

	def addChild(parentFeedId: Int, childFeedId: Int) = db.withSession { implicit session =>
		feedRelates += (parentFeedId, childFeedId)
	}

	def getChildren(feedId: Int): Seq[Int] = db.withSession { implicit session =>
		feedRelates.filter(_.parentFeedId === feedId).list.map(_._2)
	}

	def delete(feedId: Int): Boolean = db.withSession { implicit session =>
		feeds.filter(_.id === feedId).delete > 0
	}
	
	def recent(size: Int): Seq[Feed] = db.withSession { implicit session =>
		feeds.sortBy(_.dateCreated.desc).take(size).run.reverse
	}
	
	def count: Int = db.withSession { implicit session => feeds.length.run }
}