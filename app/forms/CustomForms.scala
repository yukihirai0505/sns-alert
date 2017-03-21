package forms

import play.api.data.Forms._
import play.api.data.{Form, FormError}
import play.api.i18n.MessagesApi

import models.Entities.AccountEntity


/**
  * Created by yukihirai on 2017/03/20.
  */
object VerifyMailForms extends CustomValidate{
  case class VerifyMailForm(email: Option[String] = None, expireDateTime: Option[String] = None)(implicit messagesApi: MessagesApi) { def filler = verifyMailForm.fill(this) }
  def verifyMailForm(implicit messagesApi: MessagesApi) = Form(
    mapping(
      "email" -> optional(emailValidate)
      , "expireDateTime" -> optional(text)
    )(VerifyMailForm.apply)(VerifyMailForm.unapply)
  )
  def extendValidate(frm: Form[VerifyMailForm], account: AccountEntity)(implicit messagesApi: MessagesApi): Form[VerifyMailForm] = {
    val errors: Seq[FormError] = frm.errors
    if (!frm.hasErrors) {
      def isDuplicated(email: String) = account.user.flatMap(u => Some(u.email.contains(email))).fold(false){ x => x}
      val newErrors: Seq[FormError] =  frm.get.email match {
        case Some(x) if isDuplicated(x) => errors :+ FormError("globalError", messagesApi("error.email.current"))
        case None => errors :+ FormError("globalError", messagesApi("error.required"))
        case _ => errors
      }
      frm.copy(errors = newErrors)
    }else frm
  }

  def extendRequiredValidate(frm: Form[VerifyMailForm])(implicit messagesApi: MessagesApi): Form[VerifyMailForm] = {
    val errors: Seq[FormError] = frm.errors
    if (!frm.hasErrors) {
      val newErrors: Seq[FormError] =  frm.get.email match {
        case Some(x) => errors
        case None =>
          errors :+ FormError("globalError", messagesApi("error.email.required"))
      }
      frm.copy(errors = newErrors)
    }else frm
  }
}

object LoginForms extends CustomValidate {
  case class LoginForm(email: String, password: String)(implicit messagesApi: MessagesApi){def filler = loginForm.fill(this)}
  def loginForm(implicit messagesApi: MessagesApi) = Form(
    mapping(
      "email" -> email
      , "password" -> passwordValidate
    )(LoginForm.apply)(LoginForm.unapply)
  )
}