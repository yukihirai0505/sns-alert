package daos

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider

import models.Tables.{User, UserRow}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by yukihirai on 2017/03/18.
  */
@Singleton
class UserDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._


  def add(user: UserRow): Future[UserRow] = {
    db.run(User returning User.map(_.id) += user).map(newId => user.copy(id = Some(newId)))
  }

  def update(user: UserRow): Future[String] = {
    db.run(User.filter(_.id === user.id.get).update(user)).map(_ => "User successfully updated").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def getById(id: Int): Future[Option[UserRow]] = {
    db.run(User.filter(_.id === id).result.headOption)
  }

  def getByInstagramId(instagramId: String): Future[Option[UserRow]] = {
    db.run(User.filter(_.instagramId === instagramId).result.headOption)
  }

  def getByFacebookId(facebookId: String): Future[Option[UserRow]] = {
    db.run(User.filter(_.facebookId === facebookId).result.headOption)
  }

  def getByEmail(email: String): Future[Option[UserRow]] = {
    db.run(User.filter(_.email === email).result.headOption)
  }

  def getByEmailAndPass(email: String, pass: String): Future[Option[UserRow]] = {
    db.run(User.filter(_.email === email).filter(_.password === pass).result.headOption)
  }

  def delete(id: Int): Future[Int] = {
    db.run(User.filter(_.id === id).delete)
  }

  def listAll: Future[Seq[UserRow]] = {
    db.run(User.result)
  }
}