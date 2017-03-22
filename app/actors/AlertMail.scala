package actors

import javax.inject.Singleton

import play.api.Play
import play.api.db.slick.DatabaseConfigProvider

import akka.actor.Actor
import models.Tables.User
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * author Yuki Hirai on 2017/03/22.
  */
@Singleton
class AlertMail extends Actor {

  override def receive = {
    case msg: String =>
      println(msg)
      // FIXME is it possible inject DatabaseConfigProvider??
      def listAll = {
        val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
        import dbConfig._
        import driver.api._
        db.run(User.result)
      }
      listAll.flatMap { users =>
        Future successful users.foreach(u => println(u.email))
      }
  }

}
