package actors

import javax.inject.Singleton

import akka.actor.Actor
import com.google.inject.Inject
import com.yukihirai0505.sFacebook.Facebook
import daos.{SplashPostDAO, UserDAO}
import play.api.Logger.logger
import play.api.db.slick.DatabaseConfigProvider

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
              val facebook = new Facebook(accessToken)
              facebook.getComments(facebookPostId).flatMap {
                case Some(comments) =>
                  if (comments.data.isEmpty)
                    facebook.deletePost(facebookPostId).flatMap {
                      case Some(r) => Future successful {
                        if (r.success) delete(postId.get)
                        else false
                      }
                      case None => Future successful false
                    }
                  else delete(postId.get)
                case None => delete(postId.get)
              }
            }
          }
          Future successful true
        }
      }

      splashPosts
  }
}