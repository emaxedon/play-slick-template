package services

import scala.slick.driver.PostgresDriver.simple._
import play.api.Play.current
import play.api.Logger
import java.sql.Timestamp
import models.Data
import util._
import helpers._

class DataTable(tag: Tag) extends Table[Data](tag, "datas") {

	def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
	def feedId = column[Int]("feed_id", O.NotNull)
	def feedName = column[String]("feed_name", O.NotNull)
	def feedPicture = column[Option[String]]("feed_picture")
	def feedCover = column[Option[String]]("feed_cover")
	def version = column[Long]("version", O.NotNull)
	def network = column[String]("network", O.NotNull)
	def media = column[String]("media", O.NotNull)
	def mediaUrl = column[String]("media_url")
	def previewUrl = column[String]("preview_url")
	def text = column[String]("text")
	def date = column[Timestamp]("date", O.NotNull)

	def feed = foreignKey("datas_feed_fk", feedId, TableQuery[FeedTable])(_.id, onDelete=ForeignKeyAction.Cascade)

	def * = (id.?, feedId, feedName, feedPicture, feedCover, version, network, media, mediaUrl, previewUrl, text, date) <> (Data.tupled, Data.unapply)
	
	def idx = index("datas_date_idx", date)
}

object DataService {

	val datas = TableQuery[DataTable]

	val db = play.api.db.slick.DB

	def find(id: Int): Option[Data] = db.withSession { implicit session =>
		datas.filter(_.id === id).list.headOption
	}

	def list(feedId: Int): Seq[Data] = db.withSession { implicit session =>
		datas.filter(_.feedId === feedId).sortBy(_.date.desc).list
	}

	// page is 1 based (i.e. first page is page #1)
	def listPage(feedIds: Seq[Int], page: Int, pageSize: Int): Seq[Data] = db.withSession { implicit session =>
		datas.sortBy(_.date.desc).filter(_.feedId inSetBind feedIds).drop((page - 1)*pageSize).take(pageSize).list
	}

	def create(feedId: Int, feedName: String, feedPicture: Option[String], feedCover: Option[String], network: String, media: String, mediaUrl: String, previewUrl: String, text: String, date: Timestamp): Option[Data] = db.withSession { implicit session =>
		val dataId = (datas returning datas.map(_.id)) +=
			Data(
				feedId = feedId,
				feedName = feedName,
				feedPicture = feedPicture,
				feedCover = feedCover,
				network = network,
				media = media,
				mediaUrl = mediaUrl,
				previewUrl = previewUrl,
				text = text,
				date = date)
			
		find(dataId)
	}

	def remove(id: Int) = db.withSession { implicit session => 
		datas.filter(_.id === id).delete 
	}
	
}