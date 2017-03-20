package controllers

import javax.inject.{Inject, Singleton}

import com.yukihirai0505.Authentication
import com.yukihirai0505.model.{ResponseType, Scope}
import configurations.InstagramConfig
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc._
import services.TopService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class TopC @Inject()(dbConfigProvider: DatabaseConfigProvider)
  extends TopService(dbConfigProvider)
    with InstagramConfig
{

  def index = Action.async { implicit req: Request[_] =>
    listAll.flatMap { users =>
      Future successful Ok(views.html.index(authUrl(req)))
    }
  }

}
