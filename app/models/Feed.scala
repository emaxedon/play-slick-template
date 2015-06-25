package models

import play.api.libs.json.Json
import java.sql.Timestamp
import helpers._


/** Reflects a row in the "feeds" table */
case class Feed(
	id: Option[Int] = None,
	version: Long = 1,
	category: String,
	name: String,
	facebookPicture: Option[String] = None,
	facebookCover: Option[String] = None,
	facebookApi: Option[String],
	twitterApi: Option[String],
	instagramApi: Option[String],
	youtubeApi: Option[String],
	dateCreated: Timestamp = now,
	dateUpdated: Timestamp = now,
	location: String,
	latitude: Double,
	longitude: Double
)

/** For Feed create/update */
case class FeedDetails(
	category: String,
	name: String,
	facebookApi: Option[String],
	twitterApi: Option[String],
	instagramApi: Option[String],
	youtubeApi: Option[String],
	location: String
)

/** Holds feed response data (to be serialized) */
case class FeedJson (
	id: Int,
	version: Long,
	category: String,
	name: String,
	facebookPicture: Option[String],
	facebookCover: Option[String],
	facebookApi: Option[String],
	twitterApi: Option[String],
	instagramApi: Option[String],
	youtubeApi: Option[String],
	dateCreated: Timestamp,
	dateUpdated: Timestamp,
	location: String,
	latitude: Double,
	longitude: Double,
	relatedFeeds: Seq[Int]
) {
	def this(f: Feed, relatedFeeds: Seq[Int]) = this(id = f.id.get, version = f.version, category = f.category, name = f.name, facebookPicture = f.facebookPicture, facebookCover = f.facebookCover, f.facebookApi, f.twitterApi, f.instagramApi, f.youtubeApi,
		dateCreated = f.dateCreated, dateUpdated = f.dateUpdated, location = f.location, latitude = f.latitude, longitude = f.longitude, relatedFeeds = relatedFeeds)
}

/** Provides for JSON serializing/deserializing FeedJson instances */
object FeedJson {

	implicit val feedJsonFormat = Json.format[FeedJson]

}

/** Provides for JSON serializing/deserializing FeedDetails instances */
object FeedDetails {

	implicit val feedDetailsFormat = Json.format[FeedDetails]

}