package services

import scala.slick.driver.PostgresDriver.simple._
import play.api.Play.current
import play.api.Logger
import org.mindrot.jbcrypt._
import java.sql.Timestamp
import models.{UserDetails, UserUpdate, User, ResetPassword}
import helpers._
import java.io.File
import com.github.tototoshi.csv._


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

class ResetPasswordTable(tag: Tag) extends Table[ResetPassword](tag, "reset_password_tokens") {

	def userId = column[Int]("user_id", O.PrimaryKey)
	def passwordToken = column[String]("password_token", O.NotNull)

	def user = foreignKey("reset_password_tokens_user_fk", userId, TableQuery[UserTable])(_.id)

	def * = (userId, passwordToken) <> (ResetPassword.tupled, ResetPassword.unapply)
}

object UserService {

	val users = TableQuery[UserTable]
	val resetPasswords = TableQuery[ResetPasswordTable]
	val db = play.api.db.slick.DB

	def find(id: Int): Option[User] = db.withSession { implicit session =>
		users.filter(_.id === id).list.headOption
	}

	def find(email: String): Option[User] = db.withSession { implicit session =>
		users.filter(_.email === email).list.headOption
	}

	def remove(id: Int) = db.withSession { implicit session => users.filter(_.id === id).delete }
	
	def update(id: Int, details: UserUpdate) = db.withSession { implicit session =>
		var result = 0

		if (details.email != None)
			result = users.filter(_.id === id).map( u => u.email ).update( details.email.get )
		if (details.role != None)
			result = users.filter(_.id === id).map( u => u.role ).update( details.role.get )
		if (details.location != None)
			result = users.filter(_.id === id).map( u => u.location ).update( details.location.get )

		result
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
}