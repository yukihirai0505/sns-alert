package controllers

import javax.inject.{Inject, Singleton}

import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.i18n.MessagesApi
import play.api.mvc._

import configurations.InstagramConfig
import services.MyPageService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by yukihirai on 2017/03/18.
  */
@Singleton
class MyPageC @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, cache: CacheApi, override implicit val messagesApi: MessagesApi)
  extends MyPageService(dbConfigProvider, cache, messagesApi)
    with InstagramConfig
{

  def index = Action.async { implicit req: Request[_] =>
    getMyPageViewDto.flatMap {
      case Left(_) => Future successful Redirect(routes.LoginC.login().url)
      case Right(v) => Future successful Ok(views.html.MyPageC.index(v))
    }
  }

  def delete = Action.async { implicit req: Request[_] =>
    deleteAccount.flatMap { _ =>
      Future successful Redirect(routes.LoginC.login().url)
    }
  }

}
