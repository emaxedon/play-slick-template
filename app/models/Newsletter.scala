package models

import play.api.libs.json.Json
import java.sql.Timestamp
import helpers._

/** Reflects a row in the "newsletters" table */
case class Newsletter(
	id: Option[Int] = None,
	version: Long = 1,
	subject: String,
	text: String,
	status: Int = 0,
	dateSent: Option[Timestamp] = None,
	dateCreated: Timestamp = now,
	dateUpdated: Timestamp = now
)

/** For Newsletter create */
case class NewsletterDetails(
	subject: String,
	text: String,
	status: Int
)

case class NewsletterUpdate(
	subject: Option[String],
	text: Option[String],
	status: Option[Int]
)

case class NewsletterJson(
	id: Int,
	version: Long,
	subject: String,
	text: String,
	status: Int,
	dateSent: Option[Timestamp],
	dateCreated: Timestamp, 
	dateUpdated: Timestamp
)  {
	def this(n: Newsletter) = this(id = n.id.get, version = n.version, subject = n.subject, text = n.text, 
		status = n.status, dateSent = n.dateSent, dateCreated = n.dateCreated, dateUpdated = n.dateUpdated)
}

/** Provides for JSON serializing/deserializing Newssletter instances */
object NewsletterJson {

	implicit val newsletterJsonFormat = Json.format[NewsletterJson]

}

/** Provides for JSON serializing/deserializing NewsletterDetails instances */
object NewsletterDetails {

	implicit val newsletterDetailsFormat = Json.format[NewsletterDetails]

}

object NewsletterUpdate {

	implicit val newsletterUpdateFormat = Json.format[NewsletterUpdate]

}