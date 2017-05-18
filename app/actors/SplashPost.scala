package actors

import javax.inject.Singleton

import play.api.Logger.logger
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider

import akka.actor.Actor
import com.yukihirai0505.sFacebook.Facebook
import com.yukihirai0505.sFacebook.auth.AccessToken
import models.Tables.{SplashPost, User}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class SplashPost extends Actor {
  override def receive = {
    case msg: String =>
      logger.info(msg)

      def splashPosts = {
        val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
        import dbConfig._
        import driver.api._
        import com.github.tototoshi.slick.MySQLJodaSupport._
        db.run(SplashPost.filter(_.postDatetime <= new org.joda.time.DateTime().minusDays(1)).result).flatMap { posts =>
          posts.map { post =>
            val postId = post.id
            val userId = post.userId.toInt
            val facebookPostId = post.postId
            db.run(User.filter(_.id === userId).result.head).map { user =>
              val accessToken = user.facebookAccessToken.get
              new Facebook(AccessToken(accessToken)).deletePost(facebookPostId).flatMap {
                case Some(r) => Future successful {
                  if (r.success) db.run(SplashPost.filter(_.id === postId).delete)
                  else false
                }
                case None => Future successful false
              }
            }
          }
          Future successful true
        }
      }

      splashPosts
  }
}