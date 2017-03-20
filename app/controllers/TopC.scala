package controllers

import javax.inject.{Inject, Singleton}

import configurations.InstagramConfig
import play.api.Environment
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc._
import services.TopService

import scala.concurrent.Future

@Singleton
class TopC @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment)
  extends TopService(dbConfigProvider)
    with InstagramConfig
{

  def index = Action.async { implicit req: Request[_] =>
    Future successful Ok(views.html.index(AUTH_URL(req, env)))
  }

}
