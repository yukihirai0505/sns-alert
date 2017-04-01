package services

import javax.inject.Inject

import akka.actor.ActorSystem
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import configurations.FacebookConfig
import daos.UserDAO
import models.Entities.AccountEntity
import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.{Controller, Request}
import spracebook.SprayClientFacebookGraphApi
import spray.can.Http
import utils.SessionUtil

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps


/**
  * Created by yukihirai on 2017/03/20.
  */
class FacebookService @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, implicit val cache: CacheApi)
  extends UserDAO(dbConfigProvider) with Controller with FacebookConfig {

  implicit val system = ActorSystem()
  implicit val timeout = Timeout(10 seconds)

  def callback(code: String)(implicit req: Request[_]): Future[Option[AccountEntity]] = {
    // firstly using this endpoint for get access token https://graph.facebook.com/oauth/access_token?client_id=%s&client_secret=%s&code=%s&redirect_uri=%s
    // FIXME this api client doesn not work ... I should make api client like sInstagram ... orz
    val facebook: Future[SprayClientFacebookGraphApi] = for {
      Http.HostConnectorInfo(connector, _) <- IO(Http) ? Http.HostConnectorSetup("graph.facebook.com", 443, sslEncryption = true)
    } yield {
      new SprayClientFacebookGraphApi(connector)
    }
    facebook.flatMap(_.getAccessToken(ID, SECRET, code, REDIRECT_URL(req, env))) flatMap { r =>
      println(r.access_token)
      Future successful None
    }
  }
}

