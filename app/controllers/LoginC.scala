package controllers

import javax.inject.{Inject, Singleton}

import play.api.Environment
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc._

import configurations.InstagramConfig
import dtos.ViewDto.{HeadTagInfo, ViewDto}
import services.TopService

import scala.concurrent.Future

@Singleton
class LoginC @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment)
  extends TopService(dbConfigProvider)
    with InstagramConfig
{

  def login = Action.async { implicit req: Request[_] =>
    val viewDto = ViewDto(
      headTagInfo = HeadTagInfo(title = "Login")
    )
    Future successful Ok(views.html.LoginC.login(viewDto))
  }

}
