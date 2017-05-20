package daos

import javax.inject.{Inject, Singleton}

import com.github.tototoshi.slick.MySQLJodaSupport._
import models.Tables.{SplashPost, SplashPostRow}
import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by yukihirai on 2017/03/18.
  */
@Singleton
class SplashPostDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._


  def add(splashPost: SplashPostRow): Future[SplashPostRow] = {
    db.run(SplashPost returning SplashPost.map(_.id) += splashPost).map(newId => splashPost.copy(id = Some(newId)))
  }

  def update(splashPost: SplashPostRow): Future[String] = {
    db.run(SplashPost.filter(_.id === splashPost.id.get).update(splashPost)).map(_ => "SplashPost successfully updated").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def getById(id: Int): Future[Option[SplashPostRow]] = {
    db.run(SplashPost.filter(_.id === id).result.headOption)
  }

  def getByUserId(userId: Long): Future[Seq[SplashPostRow]] = {
    db.run(SplashPost.filter(_.userId === userId).result)
  }

  def delete(id: Int): Future[Int] = {
    db.run(SplashPost.filter(_.id === id).delete)
  }

  def listAll: Future[Seq[SplashPostRow]] = {
    db.run(SplashPost.result)
  }

  def getSplashPosts: Future[Seq[SplashPostRow]] = {
    db.run(SplashPost.filter(_.splashDatetime <= new DateTime()).result)
  }
}