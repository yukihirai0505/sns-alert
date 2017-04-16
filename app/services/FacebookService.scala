package services

import javax.inject.Inject

import com.yukihirai0505.sFacebook.auth.AccessToken
import com.yukihirai0505.sFacebook.http.Response
import configurations.FacebookConfig
import daos.UserDAO
import models.Entities.AccountEntity
import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.{Controller, Request}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps


/**
  * Created by yukihirai on 2017/03/20.
  */
class FacebookService @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, implicit val cache: CacheApi)
  extends UserDAO(dbConfigProvider) with Controller with FacebookConfig {

  def callback(code: String)(implicit req: Request[_]): Future[Option[AccountEntity]] = {
    ACCESS_TOKEN(code, req, env).flatMap {
      case Response(Some(token: AccessToken), _, _) =>
        println(token.token)
        // TODO: save data and login following instagram callback
        Future successful None
      case _ => Future successful None
    }
  }
}

