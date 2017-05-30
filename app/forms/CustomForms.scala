package forms

import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.MessagesApi

/**
  * Created by yukihirai on 2017/03/20.
  */
object VerifyMailForms extends CustomValidate {

  case class VerifyMailForm(email: Option[String] = None, password: Option[String] = None, expireDateTime: Option[String] = None)(implicit messagesApi: MessagesApi)

  def verifyMailForm(implicit messagesApi: MessagesApi) = Form(
    mapping(
      "email" -> optional(emailValidate)
      , "password" -> optional(passwordValidate)
      , "expireDateTime" -> optional(text)
    )(VerifyMailForm.apply)(VerifyMailForm.unapply)
  )
}

object LoginForms extends CustomValidate {

  case class LoginForm(email: String, password: String)(implicit messagesApi: MessagesApi)

  def loginForm(implicit messagesApi: MessagesApi) = Form(
    mapping(
      "email" -> emailValidate
      , "password" -> passwordValidate
    )(LoginForm.apply)(LoginForm.unapply)
  )
}

object MyPageForms extends CustomValidate {

  case class MyPageForm(keywords: Option[String])(implicit messagesApi: MessagesApi)

  def myPageForm(implicit messagesApi: MessagesApi) = Form(
    mapping(
      "keywords" -> optional(text)
    )(MyPageForm.apply)(MyPageForm.unapply)
  )
}

object FacebookPostForms extends CustomValidate {

  case class FacebookPostForms(message: String, splashType: Int)(implicit messagesApi: MessagesApi)

  def facebookPostForm(implicit messagesApi: MessagesApi) = Form(
    mapping(
      "message" -> text
      , "splashType" -> number
    )(FacebookPostForms.apply)(FacebookPostForms.unapply)
  )
}

object ReserveInstagramPostForms extends CustomValidate {

  case class ReserveInstagramPostForms(username: String, password: String, caption: String, reserveTime: String)(implicit messagesApi: MessagesApi)

  def postForms(implicit messagesApi: MessagesApi) = Form(
    mapping(
      "username" -> text
      , "password" -> text
      , "caption" -> text
      , "reserveTime" -> text
    )(ReserveInstagramPostForms.apply)(ReserveInstagramPostForms.unapply)
  )
}