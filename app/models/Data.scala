package models

import play.api.libs.json.Json
import java.sql.Timestamp
import helpers._

/** Reflects a row in the "datas" table */
case class Data(
	id: Option[Int] = None,
	feedId: Int,
	feedName: String,
	feedPicture: Option[String],
	feedCover: Option[String],
	version: Long = 1,
	network: String,
	media: String,
	mediaUrl: String,
	previewUrl: String,
	text: String,
	date: Timestamp = now
)

/** For Data create/update */
case class DataJson(
	id: Int,
	feedId: Int,
	feedName: String,
	feedPicture: Option[String],
	feedCover: Option[String],
	network: String,
	media: String,
	mediaUrl: String,
	previewUrl: String,
	text: String,
	date: Timestamp
) {
	def this(data: Data) = this(
		id = data.id.get,
		feedId = data.feedId,
		feedName = data.feedName,
		feedPicture = data.feedPicture,
		feedCover = data.feedCover,
		network = data.network,
		media = data.media, 
		mediaUrl = data.mediaUrl,
		previewUrl = data.previewUrl,
		text = data.text,
		date = data.date)
}


/** Provides for JSON serializing/deserializing FeedDetails instances */
object DataJson {

	implicit val dataDetailsFormat = Json.format[DataJson]

}