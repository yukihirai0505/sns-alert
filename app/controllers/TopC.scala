package controllers

import javax.inject._

import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc._
import services.TopService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class TopC @Inject()(dbConfigProvider: DatabaseConfigProvider) extends TopService(dbConfigProvider) {

  def index = Action.async {
    listAll.flatMap { users =>
      Future successful Ok(views.html.index(s"users: ${users.length}"))
    }
  }

}
