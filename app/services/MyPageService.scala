package services

import javax.inject.Inject

import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Controller, Request, RequestHeader}

import configurations.InstagramConfig
import controllers.BaseTrait
import daos.UserDAO
import dtos.ViewDto.{HeadTagInfo, ViewDto}
import forms.MyPageForms
import models.Entities.AccountEntity
import utils.SessionUtil

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by yukihirai on 2017/03/20.
  */
class MyPageService @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, implicit val cache: CacheApi, implicit val messagesApi: MessagesApi)
  extends UserDAO(dbConfigProvider) with Controller with BaseTrait with I18nSupport with InstagramConfig {

  val account: (RequestHeader) => AccountEntity = (req: RequestHeader) => SessionUtil.getAccount(req, cache)

  val viewDto: (RequestHeader, String) => ViewDto = (req: RequestHeader, headTitle: String) =>
    createViewDto(req, account(req), HeadTagInfo(title = headTitle)).copy(
      instagramAuthUrl = Some(AUTH_URL(req, env))
    )

  def getMyPageViewDto(implicit req: Request[_]): Future[Either[ViewDto, ViewDto]] = {
    val v = viewDto(req, "MyPage")
    Future successful {
      if (account(req).isLogin) {
        Right(v)
      } else Left(v)
    }
  }

  def updateAccount(implicit req: Request[_]): Future[Either[ViewDto, ViewDto]] = {
    val frm = MyPageForms.myPageForm.bindFromRequest()
    val v = viewDto(req, "MyPage")
    if (frm.hasErrors || !account(req).isLogin) {
      Future successful Left(v)
    } else {
      val newUser = account(req).user.get.copy(keywords = frm.get.keywords)
      update(newUser).flatMap { _ =>
        SessionUtil.setAccount(account(req).session, account(req).copy(user = Some(newUser)))
        Future successful Right(v)
      }
    }
  }

  def deleteAccount(implicit req: Request[_]): Future[Boolean] = {
    if (account(req).isLogin) {
      delete(account(req).user.flatMap(_.id).get).flatMap { _ =>
        SessionUtil.delSession(account(req))
        Future successful true
      }
    }
    Future successful false
  }
}