package models

import play.api.libs.json.Json
import java.sql.Timestamp
import helpers._

/** Reflects a row in the "users" table */
case class User(
	id: Option[Int] = None,
	version: Long = 1,
	role: String = "user",
	email: String, 
	password: String,
	dateCreated: Timestamp = now,
	dateUpdated: Timestamp = now,
	location: String,
	latitude: Double,
	longitude: Double
)

/** Holds validated request JSON user credentials */
case class UserAuth(
	email: String,
	password: String
)

/** Holds validated request JSON user registration */
case class UserDetails(
	email: String,
	password: String,
	role: Option[String] = Some("user"),
	location: String
)

/** Holds user identity response data (to be serialized) */
case class UserJson (
	id: Int,
	version: Long,
	role: String,
	email: String,
	dateCreated: Timestamp,
	dateUpdated: Timestamp,
	location: String,
	latitude: Double,
	longitude: Double
) {
	def this(user: User) = this(id = user.id.get, version = user.version, role = user.role, email = user.email,
								dateCreated = user.dateCreated, dateUpdated = user.dateUpdated,
								location = user.location, latitude = user.latitude, longitude = user.longitude
							   )
}

case class ResetPassword(
	userId: Int,
	passwordToken: String
)

/** Provides for JSON serializing/deserializing UserJson instances */
object UserJson {

	implicit val userJsonFormat = Json.format[UserJson]

}

/** Provides for JSON serializing/deserializing UserAuth instances */
object UserAuth {

	implicit val userAuthFormat = Json.format[UserAuth]

}

/** Provides for JSON serializing/deserializing UserDetails instances */
object UserDetails {

	implicit val userRegistrationFormat = Json.format[UserDetails]

}