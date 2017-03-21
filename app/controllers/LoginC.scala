package controllers

import javax.inject.{Inject, Singleton}

import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.i18n.MessagesApi
import play.api.mvc._

import configurations.InstagramConfig
import services.LoginService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by yukihirai on 2017/03/18.
  */
@Singleton
class LoginC @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, cache: CacheApi, override implicit val messagesApi: MessagesApi)
  extends LoginService(dbConfigProvider, env, cache, messagesApi)
    with InstagramConfig
{

  def login: Action[AnyContent] = Action.async { implicit req: Request[_] =>
    getLoginViewDto.flatMap {
      case Left(_) => Future successful Redirect(routes.MyPageC.index().url)
      case Right(v) => Future successful Ok(views.html.LoginC.login(v))
    }
  }

  def confirm: Action[AnyContent] = Action.async { implicit req: Request[_] =>
    getConfirmViewDto.flatMap {
      case Left(l) => Future.successful(Ok(views.html.LoginC.login(l)))
      case Right(l) => Future.successful(Redirect(routes.MyPageC.index()).withSession(l.account.get.session))
    }
  }

  def logout: Action[AnyContent] = Action.async { implicit req: Request[_] =>
    doLogout.flatMap { _ =>
      Future successful Redirect(routes.TopC.index().url).withNewSession
    }
  }

}
