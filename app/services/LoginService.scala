package services

import javax.inject.Inject

import play.api.cache.CacheApi
import play.api.data.{Form, FormError}
import play.api.db.slick.DatabaseConfigProvider
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Controller, Request}

import controllers.BaseTrait
import daos.UserDAO
import dtos.ViewDto.{HeadTagInfo, ViewDto}
import forms.LoginForms.{LoginForm, loginForm}
import forms.VerifyMailForms.{VerifyMailForm, verifyMailForm}
import models.Entities.AccountEntity
import models.Tables.UserRow
import utils.{HashUtil, SessionUtil}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by yukihirai on 2017/03/20.
  */
class LoginService @Inject()(dbConfigProvider: DatabaseConfigProvider, implicit val cache: CacheApi, implicit val messagesApi: MessagesApi)
  extends UserDAO(dbConfigProvider) with Controller with BaseTrait with I18nSupport {

  def getLoginViewDto(implicit req: Request[_]): Future[Either[ViewDto, ViewDto]] = {
    val account: AccountEntity = SessionUtil.getAccount
    val loginFrm: Form[LoginForm] = loginForm.bindFromRequest()(req).discardingErrors
    val verifyFrm: Form[VerifyMailForm] = verifyMailForm.bindFromRequest()
    val headTagInfo = HeadTagInfo(
      title = "Login"
    )
    val viewDto: ViewDto = createViewDto(req, account, headTagInfo).copy(
      account = Some(account), loginForm = Some(loginFrm), verifyMailForm = Some(verifyFrm)
    )
    Future successful {
      if(account.isLogin) {
        Left(viewDto)
      }else Right(viewDto)
    }
  }

  def getConfirmViewDto(implicit req: Request[_]): Future[Either[ViewDto, ViewDto]] = {
    val account: AccountEntity = SessionUtil.getAccount
    val headTagInfo = HeadTagInfo(
      title = "Login"
    )
    extendValidate(loginForm.bindFromRequest()).flatMap { case (frm, user) =>
      val viewDto: ViewDto = createViewDto(req, account, headTagInfo).copy(loginForm = Some(frm))
      if(frm.hasErrors) {
        Future successful Left(viewDto)
      } else {
        doLogin(account, user) map {
          account => Right(viewDto.copy(account = Some(account)))
        }
      }
    }
  }

  def doLogout(implicit req: Request[_]): Future[AccountEntity] = SessionUtil.delSession(cache, SessionUtil.getAccount)

  private def extendValidate(frm: Form[LoginForm]): Future[(Form[LoginForm], Option[UserRow])] = {
    val errors: Seq[FormError] = frm.errors
    val email = frm("email").value.getOrElse("")
    val pass = HashUtil.passwordGenerator(frm("password").value.getOrElse(""))
    getByEmailAndPass(email, pass) flatMap {
      case None =>
        val newErrors: Seq[FormError] = errors :+ FormError("globalError", messagesApi("error.login.failure"))
        Future successful(frm.copy(errors = newErrors), None)
      case Some(user) => Future successful(frm, Some(user))
    }
  }

  private def doLogin(account: AccountEntity, user: Option[UserRow])(implicit req: Request[_]): Future[AccountEntity] = {
    val loginAccount = SessionUtil.refreshSession(account.copy(user = user))
    Future successful loginAccount
  }
}