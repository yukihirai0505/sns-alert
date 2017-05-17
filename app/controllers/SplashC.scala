package controllers

import javax.inject._

import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc._
import services.SplashService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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

}
