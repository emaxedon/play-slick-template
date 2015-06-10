package models

import play.api.libs.json.Json
import java.sql.Timestamp
import helpers._

/** Reflects a row in the "datas" table */
case class Data(
	id: Option[Int] = None,
	feedId: Int,
	version: Long = 1,
	network: String,
	media: String,
	url: String,
	text: String,
	date: Timestamp = now
)

/** For Data create/update */
case class DataJson(
	id: Int,
	network: String,
	media: String,
	url: String,
	text: String,
	date: Timestamp
) {
	def this(data: Data) = this(
		id = data.id.get, 
		network = data.network,
		media = data.media, 
		url = data.url, 
		text = data.text,
		date = data.date)
}


/** Provides for JSON serializing/deserializing FeedDetails instances */
object DataJson {

	implicit val dataDetailsFormat = Json.format[DataJson]

}