package controllers

import javax.inject.{Inject, Singleton}

import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.{Action, Request}
import services.FacebookService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by yukihirai on 2017/03/18.
  */
@Singleton
class FacebookC @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, cache: CacheApi)
  extends FacebookService(dbConfigProvider, env, cache)
{


  def test = Action.async { implicit req: Request[_] =>
    Future successful Redirect(AUTH_URL(req, env))
  }

  def callback(code: String) = Action.async { implicit req: Request[_] =>
    super.callback(code).flatMap {
      case None => Future successful Redirect(routes.LoginC.login().url)
      case Some(account) => Future successful Redirect(routes.MyPageC.index().url).withSession(account.session)
    }
  }
}
