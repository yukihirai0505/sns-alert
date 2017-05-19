package controllers

import javax.inject.{Inject, Singleton}

import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.i18n.MessagesApi
import play.api.libs.mailer.MailerClient
import play.api.mvc._

import configurations.InstagramConfig
import services.SignUpService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by yukihirai on 2017/03/18.
  */
@Singleton
class SignUpC @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, cache: CacheApi, override implicit val messagesApi: MessagesApi, mailerClient: MailerClient)
  extends SignUpService(dbConfigProvider, env, cache, messagesApi, mailerClient: MailerClient)
    with InstagramConfig {

  def index: Action[AnyContent] = Action.async { implicit req: Request[_] =>
    sentMail.flatMap {
      case Left(v) => Future successful Ok(views.html.LoginC.login(v))
      case Right(_) => Future successful Ok("sent register mail")
    }
  }

  def signUp(hash: String) = Action.async { implicit req: Request[_] =>
    doSignUp(hash).flatMap {
      case Left(_) => Future successful Ok("expired")
      case Right(a) => Future successful Redirect(routes.MyPageC.index().url).withSession(a.session)
    }

  }
}
