package services

import javax.inject.Inject

import com.yukihirai0505.Instagram
import com.yukihirai0505.http.Response
import com.yukihirai0505.responses.auth.AccessToken
import configurations.InstagramConfig
import daos.UserDAO
import models.Entities.AccountEntity
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

  def callback(code: String)(implicit req: Request[_]): Future[Option[AccountEntity]] = {
    val account = SessionUtil.getAccount
    ACCESS_TOKEN(req, code, env) flatMap {
      case Response(Some(token: AccessToken), _, _) =>
        new Instagram(token).getCurrentUserInfo.flatMap { response =>
          response.body.flatMap(_.data.flatMap(_.id)) match {
            case Some(instagramId) =>
              if (account.isLogin) {
                val newUser = account.user.get.copy(instagramId = Some(instagramId), instagramAccessToken = Some(token.token))
                update(newUser).flatMap { _ =>
                  SessionUtil.setAccount(account.session, account.copy(user = Some(newUser)))
                  Future successful Some(account)
                }
              } else {
                getByInstagramId(instagramId).flatMap {
                  case Some(user) =>
                    val newAccount = SessionUtil.refreshSession(account.copy(user = Some(user)))
                    Future successful Some(newAccount)
                  case None => Future successful None
                }
              }
            case None => Future successful None
          }
        }
      case _ => Future successful None
    }
  }

  def removeToken(implicit req: Request[_]): Future[Boolean] = {
    val account = SessionUtil.getAccount
    if(account.isLogin) {
      val newUser = account.user.get.copy(instagramId = None, instagramAccessToken = None)
      update(newUser).flatMap { _ =>
        SessionUtil.setAccount(account.session, account.copy(user = Some(newUser)))
        Future successful true
      }
    } else {
      Future successful false
    }
  }
}

