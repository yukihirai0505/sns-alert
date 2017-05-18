package controllers

import javax.inject._

import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc._

import daos.SplashPostDAO
import models.Tables.SplashPostRow
import org.joda.time.DateTime
import services.SplashService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

/**
  * Created by yukihirai on 2017/03/18.
  */
@Singleton
class SplashC @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, cache: CacheApi)
  extends SplashService(dbConfigProvider, env, cache)
{

  def index = Action.async { implicit req: Request[_] =>
    getIndexViewDto.flatMap {
      case Left(_) => Future successful Redirect(routes.LoginC.login().url)
      case Right(v) => Future successful Ok(views.html.SplashC.index(v))
    }
  }

  def test = Action.async { implicit req: Request[_] =>
    val splashPostDAO = new SplashPostDAO(dbConfigProvider)
    val newSplashPost = SplashPostRow(
      userId = 1L, postId = "hoge", snsType = 1, postDatetime = new DateTime()
    )
    val post = Await.result(splashPostDAO.add(newSplashPost), Duration.Inf)
    val posts = Await.result(splashPostDAO.listAll, Duration.Inf)
    val r = if (post.id == posts.lastOption.flatMap(_.id)) "ok" else "false"
    Future successful Ok(r)
  }
}
