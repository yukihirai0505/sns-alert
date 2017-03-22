package services

import javax.inject.Inject

import play.api.{Environment, Mode}
import play.api.cache.CacheApi
import play.api.data.{Form, FormError}
import play.api.db.slick.DatabaseConfigProvider
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.mailer.{Email, MailerClient}
import play.api.mvc.{Controller, Request, RequestHeader}

import controllers.{BaseTrait, routes}
import daos.UserDAO
import dtos.ViewDto.{HeadTagInfo, ViewDto}
import forms.LoginForms.{LoginForm, loginForm}
import forms.VerifyMailForms.{VerifyMailForm, verifyMailForm}
import models.Entities.{AccountEntity, VerifyMailEntity}
import models.Tables.UserRow
import utils.{HashUtil, SessionUtil}

import scala.concurrent.duration._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by yukihirai on 2017/03/20.
  */
class SignUpService @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, implicit val cache: CacheApi, implicit val messagesApi: MessagesApi, mailerClient: MailerClient)
  extends UserDAO(dbConfigProvider) with Controller with BaseTrait with I18nSupport {

  private val verifyCacheKey = (hash: String) => s"VERIFY_HASH_$hash"

  def sentMail(implicit req: Request[_]): Future[Either[ViewDto, ViewDto]] = {
    val account: AccountEntity = SessionUtil.getAccount
    extendDuplicatedValidate(verifyMailForm.bindFromRequest()).flatMap { frm =>
      val headTagInfo = HeadTagInfo(
        title = "Login"
      )
      val loginFrm: Form[LoginForm] = loginForm.bindFromRequest().discardingErrors
      val viewDto: ViewDto = createViewDto(req, account, headTagInfo).copy(
        account = Some(account), loginForm = Some(loginFrm), verifyMailForm = Some(frm)
      )
      Future successful {
        if (frm.hasErrors) {
          Left(viewDto)
        } else {
          sendSignUpMail(account, frm.get.email.get, frm.get.password.get)
          Right(viewDto)
        }
      }
    }
  }

  def doSignUp(hash: String)(implicit req: Request[_]): Future[Either[AccountEntity, AccountEntity]] = {
    val account = SessionUtil.getAccount
    val entity = cache.get[VerifyMailEntity](verifyCacheKey(hash))
    cache.remove(verifyCacheKey(hash))
    entity match {
      case Some(e) =>
        val newUser = UserRow(email = e.email.get, password = HashUtil.passwordGenerator(e.password.get))
        add(newUser).flatMap { user =>
          val newAccount = account.copy(user = Some(user))
          SessionUtil.setAccount(newAccount.session, newAccount)
          Future successful Right(newAccount)
        }
      case None => Future successful Left(account)
    }
  }

  def extendDuplicatedValidate(frm: Form[VerifyMailForm])(implicit messagesApi: MessagesApi): Future[Form[VerifyMailForm]] = {
    val errors: Seq[FormError] = frm.errors
    if (!frm.hasErrors) {
      (frm.get.email, frm.get.password) match {
        case (Some(x), Some(_)) =>
          getByEmail(x) flatMap { user =>
            if (user.isDefined) {
              Future successful frm.copy(errors = errors :+ FormError("globalError", messagesApi("error.email.duplicated")))
            } else {
              Future successful frm
            }
          }
        case (None, _)=>
          Future successful frm.copy(errors = errors :+ FormError("globalError", messagesApi("error.email.required")))
        case (_, None)=>
          Future successful frm.copy(errors = errors :+ FormError("globalError", messagesApi("error.password.required")))
      }
    } else {
      Future successful frm
    }
  }

  def sendSignUpMail(account: AccountEntity,email: String, pass: String)(implicit req: RequestHeader) = {
    val hash: String = HashUtil.create
    val url = routes.SignUpC.signUp(hash).absoluteURL(secure = env.mode.equals(Mode.Prod))
    cache.set(
      verifyCacheKey(hash)
      , VerifyMailEntity(account.session, Some(email), Some(pass))
      , expiration = 1.hour
    )
    val data = Email(
      "Simple email",
      "Mister FROM <from@email.com>",
      Seq(s"Miss TO <$email>"),
      // adds attachment
      attachments = Seq(
        // AttachmentFile("attachment.pdf", new File("/some/path/attachment.pdf")),
        // adds inline attachment from byte array
        // AttachmentData("data.txt", "data".getBytes, "text/plain", Some("Simple data"), Some(EmailAttachment.INLINE)),
        // adds cid attachment
        // AttachmentFile("image.jpg", new File("/some/path/image.jpg"), contentId = Some(cid))
      ),
      // sends text, HTML or both...
      bodyText = Some(url)
    )
    mailerClient.send(data)
  }

}