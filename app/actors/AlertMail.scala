package actors

import javax.inject.Singleton

import akka.actor.Actor
import com.google.inject.Inject
import daos.UserDAO
import play.api.Logger.logger
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * author Yuki Hirai on 2017/03/22.
  */
@Singleton
class AlertMail @Inject()(dbConfigProvider: DatabaseConfigProvider) extends UserDAO(dbConfigProvider) with Actor {

  override def receive = {
    case msg: String =>
      logger.info(msg)
      listAll.flatMap { users =>
        Future successful users.foreach(u => logger.info(u.email))
      }
  }

}
