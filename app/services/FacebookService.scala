package services

import java.io.File
import javax.inject.Inject

import com.yukihirai0505.sFacebook.Facebook
import com.yukihirai0505.sFacebook.responses.auth.Oauth
import configurations.FacebookConfig
import constants.Constants
import constants.Constants.SnsType
import controllers.BaseTrait
import daos.{SplashPostDAO, UserDAO}
import forms.FacebookPostForms
import models.Entities.AccountEntity
import models.Tables.{SplashPostRow, UserRow}
import org.joda.time.DateTime
import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.i18n.MessagesApi
import play.api.libs.Files
import play.api.mvc.{Controller, MultipartFormData, Request}
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
      case Some(oauth: Oauth) =>
        new Facebook(oauth.accessToken).getUser().flatMap {
          case Some(response) =>
            if (account.isLogin) {
              val newUser = account.user.get.copy(facebookId = Some(response.id), facebookAccessToken = Some(oauth.accessToken))
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
    if (account.isLogin) {
      val newUser = account.user.get.copy(facebookId = None, facebookAccessToken = None)
      update(newUser).flatMap { _ =>
        SessionUtil.setAccount(account.session, account.copy(user = Some(newUser)))
        Future successful true
      }
    } else {
      Future successful false
    }
  }

  def postMessage(implicit req: Request[_]): Future[Either[Throwable, Boolean]] = {
    val account: AccountEntity = SessionUtil.getAccount
    val frm = FacebookPostForms.facebookPostForm.bindFromRequest()
    if (frm.hasErrors || !account.isLogin) {
      Future successful Right(false)
    } else {
      val user = account.user.get
      val message = frm.get.message
      val splashType = frm.get.splashType
      val multipartReq = req.asInstanceOf[Request[MultipartFormData[Files.TemporaryFile]]]
      try {
        val file = multipartReq.body.file(Constants.fileName).get
        if (file.filename.nonEmpty) publishPhoto(user, message, splashType, file.ref.file)
        else publishPost(user, message, splashType)
      } catch {
        case e: Exception => Future successful Left(e)
      }
    }
  }

  private def publishPhoto(user: UserRow, message: String, splashType: Int, file: File): Future[Either[Throwable, Boolean]] = {
    new Facebook(user.facebookAccessToken.get).publishPhotos(user.facebookId.get, message, file).flatMap { response =>
      val postId = response.get.postId
      saveSplashPost(user, message, splashType, postId).flatMap { _ =>
        Future successful Right(true)
      }
    }
  }

  private def publishPost(user: UserRow, message: String, splashType: Int): Future[Either[Throwable, Boolean]] = {
    new Facebook(user.facebookAccessToken.get).publishPost(user.facebookId.get, Some(message)).flatMap { response =>
      val postId = response.get.id
      saveSplashPost(user, message, splashType, postId).flatMap { _ =>
        Future successful Right(true)
      }
    }
  }

  private def saveSplashPost(user: UserRow, message: String, splashType: Int, postId: String) = {
    val splashPostDAO = new SplashPostDAO(dbConfigProvider)
    val postIds = postId.split("_")
    val link = s"https://www.facebook.com/${postIds(0)}/posts/${postIds(1)}"
    val now = new DateTime()
    val newSplashPost = SplashPostRow(
      userId = user.id.get, postId = postId, message = message, link = link, snsType = SnsType.Facebook.value, postDatetime = now, splashDatetime = now.plusHours(splashType)
    )
    splashPostDAO.add(newSplashPost)
  }
}

