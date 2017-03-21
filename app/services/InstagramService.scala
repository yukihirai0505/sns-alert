package services

import javax.inject.Inject

import com.yukihirai0505.http.Response
import com.yukihirai0505.responses.auth.AccessToken
import configurations.InstagramConfig
import daos.UserDAO
import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.{Controller, Request}
import utils.SessionUtil

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by yukihirai on 2017/03/20.
  */
class InstagramService @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, implicit val cache: CacheApi)
  extends UserDAO(dbConfigProvider) with Controller with InstagramConfig {

  def callback(code: String)(implicit req: Request[_]): Future[Boolean] = {
    val account = SessionUtil.getAccount
    if(account.isLogin) {
      ACCESS_TOKEN(req, code, env) flatMap {
        case Response(Some(token: AccessToken), _, _) =>
          update(account.user.get.copy(instagramAccessToken = Some(token.token))).flatMap { _ =>
            Future successful true
          }
        case _ => Future successful false
      }
    } else {
      Future successful false
    }
  }
}

