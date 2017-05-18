package actors

import javax.inject.Singleton

import play.api.Logger.logger
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import akka.actor.Actor
import com.google.inject.Inject
import com.yukihirai0505.sFacebook.Facebook
import com.yukihirai0505.sFacebook.auth.AccessToken
import models.Tables.{SplashPost, User}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import daos.{SplashPostDAO, UserDAO}


@Singleton
class SplashPost @Inject()(dbConfigProvider: DatabaseConfigProvider) extends SplashPostDAO(dbConfigProvider) with Actor {
  override def receive = {
    case msg: String =>
      logger.info(msg)

      def splashPosts = {
        getSplashPosts.flatMap { posts =>
          posts.map { post =>
            val userDao = new UserDAO(dbConfigProvider)
            val postId = post.id
            val userId = post.userId.toInt
            val facebookPostId = post.postId
            userDao.getById(userId).map { user =>
              val accessToken = user.get.facebookAccessToken.get
              new Facebook(AccessToken(accessToken)).deletePost(facebookPostId).flatMap {
                case Some(r) => Future successful {
                  if (r.success) delete(postId.get)
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