package daos

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider

import com.github.tototoshi.slick.MySQLJodaSupport._
import models.Tables.{ReserveInstagramPost, ReserveInstagramPostRow}
import org.joda.time.DateTime
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by yukihirai on 2017/05/30.
  */
@Singleton
class ReserveInstagramPostDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._


  def add(post: ReserveInstagramPostRow): Future[ReserveInstagramPostRow] = {
    db.run(ReserveInstagramPost returning ReserveInstagramPost.map(_.id) += post).map(newId => post.copy(id = Some(newId)))
  }

  def update(post: ReserveInstagramPostRow): Future[String] = {
    db.run(ReserveInstagramPost.filter(_.id === post.id.get).update(post)).map(_ => "ReserveInstagramPost successfully updated").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def getById(id: Int): Future[Option[ReserveInstagramPostRow]] = {
    db.run(ReserveInstagramPost.filter(_.id === id).result.headOption)
  }

  def delete(id: Int): Future[Int] = {
    db.run(ReserveInstagramPost.filter(_.id === id).delete)
  }

  def listAll: Future[Seq[ReserveInstagramPostRow]] = {
    db.run(ReserveInstagramPost.result)
  }

  def getPostList: Future[Seq[ReserveInstagramPostRow]] = {
    db.run(ReserveInstagramPost.filter(_.reserveDatetime <= new DateTime()).result)
  }
}