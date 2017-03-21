package controllers

import javax.inject._

import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc._
import services.InstagramService
import utils.SessionUtil

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by yukihirai on 2017/03/18.
  */
@Singleton
class InstagramC @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, cache: CacheApi)
  extends InstagramService(dbConfigProvider, env, cache)
{

  def callback(code: String) = Action.async { implicit req: Request[_] =>
    super.callback(code).flatMap {
      case false => Future successful Redirect(routes.LoginC.login().url)
      case true => Future successful Redirect(routes.MyPageC.index().url).withSession(SessionUtil.getAccount(req, cache).session)
    }
  }

  def remove = Action.async { implicit req: Request[_] =>
    removeToken.flatMap {
      case false => Future successful Redirect(routes.LoginC.login().url)
      case true => Future successful Redirect(routes.MyPageC.index().url)
    }
  }
}
