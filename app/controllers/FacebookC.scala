package controllers

import javax.inject.{Inject, Singleton}

import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.i18n.MessagesApi
import play.api.libs.Files
import play.api.mvc.{Action, MultipartFormData, Request}
import services.FacebookService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by yukihirai on 2017/03/18.
  */
@Singleton
class FacebookC @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, cache: CacheApi, messagesApi: MessagesApi)
  extends FacebookService(dbConfigProvider, env, cache, messagesApi) {


  def auth = Action.async { implicit req: Request[_] =>
    Future successful Redirect(AUTH_URL(req, env))
  }

  def callback(code: String) = Action.async { implicit req: Request[_] =>
    super.callback(code).flatMap {
      case None => Future successful Redirect(routes.LoginC.login().url)
      case Some(account) => Future successful Redirect(routes.MyPageC.index().url).withSession(account.session)
    }
  }

  def remove = Action.async { implicit req: Request[_] =>
    removeToken.flatMap {
      case false => Future successful Redirect(routes.LoginC.login().url)
      case true => Future successful Redirect(routes.MyPageC.index().url)
    }
  }

  def post: Action[MultipartFormData[Files.TemporaryFile]] = Action.async(parse.multipartFormData) { implicit req: Request[_] =>
    postMessage.flatMap {
      case Right(bool) =>
        if (bool) Future successful Redirect(routes.SplashC.index().url)
        else Future successful Redirect(routes.LoginC.login().url)
      case Left(_) => Future successful Redirect(routes.FacebookC.auth().url)
    }
  }

}
