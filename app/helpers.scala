package object helpers {

	import java.sql.Timestamp
	import java.util.Date
	import java.net.URLEncoder
	
	import util.Random
	import concurrent.ExecutionContext.Implicits.global
	import concurrent.{ Await, Future }
	import concurrent.duration._
	import math._
	
	import play.api.libs.functional.syntax._
	import play.api.libs.json._
	import play.api.libs.ws.WS
	import play.api.Play.current
	import play.api.Logger
	
	import akka.util.Timeout

	
	val alphanumChars = ('a' to 'z') ++ ('0' to '9')
	val alphaChars = ('a' to 'z').toVector

	def randomChar( chars: Seq[Char] ) = chars(Random.nextInt(chars.length))
	
	def randomString(size: Int) = (randomChar(alphaChars) +: (for (_ <- 1 to size - 1) yield randomChar(alphanumChars))).mkString

	def randomRange( lower: Double, upper: Double ) = Random.nextDouble*(upper - lower) + lower

	def now = new Timestamp(new Date().getTime)

	implicit val timestampReads: Reads[Timestamp] = (__ \ "time").read[Long].map{ long => new Timestamp(long) }
	implicit val timestampWrites: Writes[Timestamp] = (__ \ "time").write[Long].contramap{ (a: Timestamp) => a.getTime }
	implicit val timestampFormat: Format[Timestamp] = Format(timestampReads, timestampWrites)
	
	def geocode( address: String ): Option[Geo] = {
		implicit val timeout = Timeout(50000 milliseconds)

		val addressEncoded = URLEncoder.encode(address, "UTF-8");
		val json = WS.url("http://maps.googleapis.com/maps/api/geocode/json?address=" + addressEncoded + "&sensor=true").get()
		val future = json map {
			response => (response.json \\ "location")
		}

		Await.result(future, timeout.duration).asInstanceOf[List[JsObject]] match {
			case head :: _ => Some(Geo((head \\ "lat").head.toString.toDouble, (head \\ "lng").head.toString.toDouble))
			case Nil => None
		}
	}
	
	val equatorialRadius = 6378.1370
	val polarRadius = 6356.7523
	
	// Mean radius as defined by the International Union of Geodesy and Geophysics (IUGG)
	val R1 = (2*equatorialRadius + polarRadius)/3
	
	case class Geo( latitude: Double, longitude: Double ) {
		require( latitude >= -90 && latitude <= 90, "-90 <= latitude <= 90" )
		require( longitude >= -180 && longitude <= 180, "-180 <= longitude <= 180" )
	}
	
	def angle( dist: Double ) = dist/R1
	
	def geoBoundingBox( loc: Geo, radius: Double ) = {
		val theta = angle( radius )
		val deglat = toDegrees( theta )
		val deglng = toDegrees( theta/cos(toRadians(loc.latitude)) )
		
		(Geo(loc.latitude - deglat, loc.longitude - deglng), Geo(loc.latitude + deglat, loc.longitude + deglng))
	}
}