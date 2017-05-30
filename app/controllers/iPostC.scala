package controllers

import javax.inject._

import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.i18n.MessagesApi
import play.api.libs.Files
import play.api.mvc._

import services.iPostService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by yukihirai on 2017/03/18.
  */
@Singleton
class iPostC @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, cache: CacheApi, messagesApi: MessagesApi)
  extends iPostService(dbConfigProvider, env, cache, messagesApi: MessagesApi) {

  def index = Action.async { implicit req: Request[_] =>
    getIndexViewDto.flatMap { v =>
      Future successful Ok(views.html.iPostC.index(v))
    }
  }

  def reserve: Action[MultipartFormData[Files.TemporaryFile]] = Action.async(parse.multipartFormData) { implicit req: Request[_] =>
    savePost.flatMap { _ =>
      Future successful Redirect(routes.iPostC.index().url)
    }
  }

  def post = Action.async { implicit req: Request[_] =>
    postToInstagram.flatMap { _ =>
      Future successful Ok("")
    }
  }

}
