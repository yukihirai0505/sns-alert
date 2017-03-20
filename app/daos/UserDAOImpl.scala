package daos

import javax.inject.{Inject, Singleton}

import daos.traits.UserDAO
import models.Tables.{User, UserRow}
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by yukihirai on 2017/03/18.
  */
@Singleton
class UserDAOImpl @Inject()(dbConfigProvider: DatabaseConfigProvider) extends UserDAO {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import driver.api._


  override def add(user: UserRow): Future[String] = {
    db.run(User += user).map(_ => "User successfully added").recover {
      case ex : Exception => ex.getCause.getMessage
    }
  }

  override def get(id: Int): Future[Option[UserRow]] = {
    db.run(User.filter(_.id === id).result.headOption)
  }

  override def delete(id: Int): Future[Int] = {
    db.run(User.filter(_.id === id).delete)
  }

  override def listAll: Future[Seq[UserRow]] = {
    db.run(User.result)
  }
}