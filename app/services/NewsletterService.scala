package services

import scala.slick.driver.PostgresDriver.simple._
import play.api.Play.current
import play.api.Logger
import java.sql.Timestamp
import models.Newsletter
import util._
import helpers._

class NewsletterTable(tag: Tag) extends Table[Newsletter](tag, "newsletters") {

	def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
	def version = column[Long]("version", O.NotNull)
	def subject = column[String]("subject", O.NotNull)
	def text = column[String]("text", O.NotNull)
	def status = column[Int]("status", O.NotNull)
	def dateSent = column[Option[Timestamp]]("date_sent")
	def dateCreated = column[Timestamp]("date_created", O.NotNull)
	def dateUpdated = column[Timestamp]("date_updated", O.NotNull)

	def * = (id.?, version, subject, text, status, dateSent, dateCreated, dateUpdated) <> (Newsletter.tupled, Newsletter.unapply)

}

object NewsletterService {

	val newsletters = TableQuery[NewsletterTable]

	val db = play.api.db.slick.DB

	def find(id: Int): Option[Newsletter] = db.withSession { implicit session =>
		newsletters.filter(_.id === id).list.headOption
	}

	def list: Seq[Newsletter] = db.withSession { implicit session =>
		newsletters.sortBy(_.dateCreated.desc).list
	}

	def create(subject: String, text: String): Option[Newsletter] = db.withSession { implicit session =>
		val newsletterId = (newsletters returning newsletters.map(_.id)) +=
			Newsletter(
				subject = subject,
				text = text)
			
		find(newsletterId)
	}

	def update(id: Int, subject: Option[String], text: Option[String], status: Option[Int]) = db.withSession { implicit session =>
		var result = 0

		if (subject != None)
			result = newsletters.filter(_.id === id).map( n => n.subject ).update( subject.get )
		if (text != None)
			result = newsletters.filter(_.id === id).map( n => n.text ).update( text.get )
		if (status != None)
			if (status.get == 1) //sent
				result = newsletters.filter(_.id === id).map( n => (n.status, n.dateSent) ).update( (status.get, Some(now)) )

		result
	}

	def remove(id: Int) = db.withSession { implicit session => 
		newsletters.filter(_.id === id).delete 
	}
	
}