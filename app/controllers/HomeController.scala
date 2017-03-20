package controllers

import javax.inject._

import daos.UserDAOImpl
import models.Tables.UserRow
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(dbConfigProvider: DatabaseConfigProvider) extends Controller {

  def index = Action.async {
    val userDao = new UserDAOImpl(dbConfigProvider)
    userDao.listAll.flatMap { users =>
      val length = users.length
      userDao.add(UserRow(email = Some(s"hoge+$length@gmail.com")))
      Future successful Ok(views.html.index(s"users: $length"))
    }
  }

}
