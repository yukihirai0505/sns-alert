package services

import javax.inject.Inject

import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.{Controller, Request, RequestHeader}
import com.yukihirai0505.sFacebook.Facebook
import com.yukihirai0505.sFacebook.auth.AccessToken
import configurations.FacebookConfig
import controllers.BaseTrait
import daos.UserDAO
import dtos.ViewDto.{HeadTagInfo, ViewDto}
import forms.FacebookPostForms
import models.Entities.AccountEntity
import play.api.i18n.MessagesApi
import utils.SessionUtil

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps


/**
  * Created by yukihirai on 2017/03/20.
  */
class FacebookService @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, implicit val cache: CacheApi, implicit val messagesApi: MessagesApi)
  extends UserDAO(dbConfigProvider) with Controller with FacebookConfig with BaseTrait {

  def callback(code: String)(implicit req: Request[_]): Future[Option[AccountEntity]] = {
    val account: AccountEntity = SessionUtil.getAccount
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
    val account: AccountEntity = SessionUtil.getAccount
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

  def postMessage(implicit req: Request[_]): Future[Boolean] = {
    val account: AccountEntity = SessionUtil.getAccount
    val frm = FacebookPostForms.facebookPostForm.bindFromRequest()
    if (frm.hasErrors || !account.isLogin) {
      Future successful false
    } else {
      val user = account.user.get
      val message = frm.get.message
      new Facebook(AccessToken(user.facebookAccessToken.get)).publishPost(user.facebookId.get, Some(message)).flatMap { response =>
        // TODO: save post id and time to db
        println(s"post id: ${response.get.id}")
        Future successful true
      }
    }
  }

}

