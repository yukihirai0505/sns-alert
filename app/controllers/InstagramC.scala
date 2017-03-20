package controllers

import javax.inject._

import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc._
import services.InstagramService

import scala.concurrent.Future

@Singleton
class InstagramC @Inject()(dbConfigProvider: DatabaseConfigProvider) extends InstagramService(dbConfigProvider) {

  def callback = Action.async {
    Future successful Ok("NOT YET")
  }

}
