package services

import javax.inject.Inject

import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.{Controller, Request}

import com.yukihirai0505.sFacebook.Facebook
import com.yukihirai0505.sFacebook.auth.AccessToken
import configurations.FacebookConfig
import daos.UserDAO
import models.Entities.AccountEntity
import utils.SessionUtil

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps


/**
  * Created by yukihirai on 2017/03/20.
  */
class FacebookService @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, implicit val cache: CacheApi)
  extends UserDAO(dbConfigProvider) with Controller with FacebookConfig {

  def callback(code: String)(implicit req: Request[_]): Future[Option[AccountEntity]] = {
    val account = SessionUtil.getAccount
    ACCESS_TOKEN(code, req, env).flatMap {
      case Some(token: AccessToken) =>
        new Facebook(token).getMe().flatMap {
          case Some(response) =>
            if (account.isLogin) {
              val newUser = account.user.get.copy(facebookId = Some(response.id), facebookAccessToken = Some(token.token))
              update(newUser).flatMap { _ =>
                SessionUtil.setAccount(account.session, account.copy(user = Some(newUser)))
                Future successful Some(account)
              }
            } else {
              getByFacebookId(response.id).flatMap {
                case Some(user) =>
                  val newAccount = SessionUtil.refreshSession(account.copy(user = Some(user)))
                  Future successful Some(newAccount)
                case None => Future successful None
              }
            }
          case None => Future successful None
        }
      case _ => Future successful None
    }
  }

  def removeToken(implicit req: Request[_]): Future[Boolean] = {
    val account = SessionUtil.getAccount
    if(account.isLogin) {
      val newUser = account.user.get.copy(facebookId = None, facebookAccessToken = None)
      update(newUser).flatMap { _ =>
        SessionUtil.setAccount(account.session, account.copy(user = Some(newUser)))
        Future successful true
      }
    } else {
      Future successful false
    }
  }
}

