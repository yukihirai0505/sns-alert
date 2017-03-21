package controllers

import javax.inject.{Inject, Singleton}

import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.i18n.MessagesApi
import play.api.mvc._

import services.TopService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by yukihirai on 2017/03/18.
  */
@Singleton
class TopC @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, cache: CacheApi, override implicit val messagesApi: MessagesApi)
  extends TopService(dbConfigProvider, env, cache, messagesApi)
{


  def index: Action[AnyContent] = Action.async { implicit req: Request[_] =>
    getIndexViewDto.flatMap {
      case Left(v) => Future successful Ok(views.html.TopC.index(v)).withSession(v.account.get.session)
      case Right(v) => Future successful Redirect(routes.MyPageC.index()).withSession(v.account.get.session)
    }
  }

}
