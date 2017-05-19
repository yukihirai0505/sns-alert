package actors

import javax.inject.Singleton

import play.api.Logger.logger
import play.api.db.slick.DatabaseConfigProvider

import akka.actor.Actor
import com.google.inject.Inject
import com.yukihirai0505.sFacebook.Facebook
import daos.{SplashPostDAO, UserDAO}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


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
              new Facebook(accessToken).deletePost(facebookPostId).flatMap {
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