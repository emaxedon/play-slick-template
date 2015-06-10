package services

import scala.slick.driver.PostgresDriver.simple._
import play.api.Play.current
import play.api.Logger
import org.mindrot.jbcrypt._
import java.sql.Timestamp
import models.{UserDetails, User, ResetPassword}
import helpers._


class UserTable(tag: Tag) extends Table[User](tag, "users") {

	def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
	def version = column[Long]("version", O.NotNull)
	def role = column[String]("role", O.NotNull)
	def email = column[String]("email", O.NotNull)
	def password = column[String]("password", O.NotNull)
	def dateCreated = column[Timestamp]("date_created", O.NotNull)
	def dateUpdated = column[Timestamp]("date_updated", O.NotNull)
	def location = column[String]("location", O.NotNull)
	def latitude = column[Double]("latitude", O.NotNull)
	def longitude = column[Double]("longitude", O.NotNull)

	def idx = index("users_idx_a", email, unique = true)

	def * = (id.?, version, role, email, password, dateCreated, dateUpdated, location, latitude, longitude) <> (User.tupled, User.unapply)
}

class UserFeedTable(tag: Tag) extends Table[(Int, Int)](tag, "users_feeds") {

	def userId = column[Int]("user_id")
	def feedId = column[Int]("feed_id")

	def pk = primaryKey("pk_a", (userId, feedId))

//	def user = foreignKey("users_feeds_user_fk", userId, TableQuery[UserTable])(_.id, onDelete=ForeignKeyAction.Cascade)
	def feed = foreignKey("users_feeds_feed_fk", feedId, TableQuery[FeedTable])(_.id, onDelete=ForeignKeyAction.Cascade)

	def * = (userId, feedId)
}

class ResetPasswordTable(tag: Tag) extends Table[ResetPassword](tag, "reset_password_tokens") {

	def userId = column[Int]("user_id", O.PrimaryKey)
	def passwordToken = column[String]("password_token", O.NotNull)

	def user = foreignKey("reset_password_tokens_user_fk", userId, TableQuery[UserTable])(_.id)

	def * = (userId, passwordToken) <> (ResetPassword.tupled, ResetPassword.unapply)
}

object UserService {

	val users = TableQuery[UserTable]
	val userFeeds = TableQuery[UserFeedTable]
	val resetPasswords = TableQuery[ResetPasswordTable]
	val db = play.api.db.slick.DB

	def find(id: Int): Option[User] = db.withSession { implicit session =>
		users.filter(_.id === id).list.headOption
	}

	def find(email: String): Option[User] = db.withSession { implicit session =>
		users.filter(_.email === email).list.headOption
	}

	def remove(id: Int) = db.withSession { implicit session => users.filter(_.id === id).delete }
	
	def update(id: Int, details: UserDetails) = db.withSession { implicit session =>
		if (details.password == "")
			users.filter(_.id === id).map( u => (u.email, u.role, u.location) ).update( (details.email, details.role.get, details.location) )
		else
			users.filter(_.id === id).map( u => (u.email, u.password, u.role, u.location) ).
				update( (details.email, BCrypt.hashpw(details.password, BCrypt.gensalt()), details.role.get, details.location) )
	}
		
	def list = db.withSession { implicit session => users.list }
	
	def recent(size: Int) = db.withSession { implicit session => users.sortBy(_.dateCreated.desc).take(size).run.reverse }
	
	def prefix(s: String) = db.withSession { implicit session => users.filter(_.email.toLowerCase.startsWith(s.toLowerCase)).list }
	
	def prefixCount(s: String) = db.withSession { implicit session => users.filter(_.email.toLowerCase.startsWith(s.toLowerCase)).length.run }
	
	def count = db.withSession { implicit session => users.length.run }
	
	def authenticate(email: String, password: String): Option[User] = db.withSession { implicit session =>
		find(email) match {
			case Some(user) => if (BCrypt.checkpw(password, user.password)) Some(user) else None
			case None => None
		}
	}

	def authenticateOnce(userId: Int, passwordToken: String): Option[User] = db.withSession { implicit session =>
		resetPasswords.filter(resetPassword => resetPassword.userId === userId && resetPassword.passwordToken === passwordToken).list match {
			case List(resetPassword) =>
				resetPasswords.filter(resetPassword => resetPassword.userId === userId && resetPassword.passwordToken === passwordToken).delete
				find(userId)
			case _ => None
		}
	}

	def create(email: String, password: String, role: String, location: String, geo: Geo): Option[User] =
		db.withSession { implicit session =>
			find(email) match {
				case Some(user) => None
				case None =>
					users += User(email = email, password = BCrypt.hashpw(password, BCrypt.gensalt()), role = role,
								  location = location, latitude = geo.latitude, longitude = geo.longitude)
					find(email)
			}
		}

	def changePassword(email: String, password: String): Option[User] = db.withSession { implicit session =>
		find(email) match {
			case Some(user) =>
				users.filter(_.email === email).map(user => (user.version, user.password, user.dateUpdated)).update((user.version + 1, BCrypt.hashpw(password, BCrypt.gensalt()), now))
				
				find(email)
			case None => None
		}
	}

	def resetPassword(email: String): Option[ResetPassword] = db.withSession { implicit session =>
		find(email) match {
			case Some(user) =>
				val resetPassword = ResetPassword(userId = user.id.get, passwordToken = randomString(12))
				resetPasswords += resetPassword
				users.filter(_.email === email).map(user => (user.version, user.password, user.dateUpdated)).update((user.version + 1, BCrypt.hashpw(randomString(8), BCrypt.gensalt()), now))
				
				Some(resetPassword)
			case None => None
		}
	}
	
	def following(userId: Int): Seq[(Int, Int)] = db.withSession { implicit session => userFeeds.filter(_.userId === userId).list }
	
	def follow(userId: Int, feedId: Int) = db.withSession { implicit session =>
		userFeeds += (userId, feedId)
	}

	def unfollow(userId: Int, feedId: Int): Boolean = db.withSession { implicit session =>
		userFeeds.filter(userFeed => userFeed.userId === userId && userFeed.feedId === feedId).delete > 0
	}
}