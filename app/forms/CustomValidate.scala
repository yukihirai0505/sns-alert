package forms

import play.api.data.Forms._
import play.api.data.Mapping
import play.api.data.format.Formats._
import play.api.data.validation._
import play.api.i18n.MessagesApi

/**
  * Created by yukihirai on 2017/03/20.
  */
trait CustomValidate {

  /** *
    * regex area
    * !"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~
    * */
  private val emailRegex =
    """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""".r
  private val passwordRegex = """^[!-\~]+$""".r //全てが半角英数(記号込み)

  /** * signup & profile area ***/
  /**
    * email
    * custom validation sample.
    */
  def emailValidate(implicit messagesApi: MessagesApi): Mapping[String] = of[String] verifying emailValidateCheck

  private def emailValidateCheck(implicit messagesApi: MessagesApi): Constraint[String] = Constraint[String]("email") {
    case e if e == null => Invalid(ValidationError(messagesApi("error.email.required")))
    case e if e.isEmpty => Invalid(ValidationError(messagesApi("error.email.required")))
    case e if e.trim.isEmpty => Invalid(ValidationError(messagesApi("error.email.required")))
    case e => emailRegex.findFirstMatchIn(e).map(_ => Valid).getOrElse(Invalid(ValidationError(messagesApi("error.email.noval", e))))
  }

  /**
    * password number
    * custom validation sample.
    */
  def passwordValidate(implicit messagesApi: MessagesApi): Mapping[String] = of[String] verifying passwordValidateCheck

  private def passwordValidateCheck(implicit messagesApi: MessagesApi): Constraint[String] = Constraint[String]("password") {
    case p if p == null => Invalid(ValidationError(messagesApi("error.required")))
    case p if p.isEmpty => Invalid(ValidationError(messagesApi("error.required")))
    case p if p.trim.isEmpty => Invalid(ValidationError(messagesApi("error.required")))
    case p if p.length > 16 || p.length < 8 => Invalid(ValidationError(messagesApi("error.password.length")))
    case p if passwordRegex.findFirstMatchIn(p).fold(true) { x => false } => Invalid(ValidationError(messagesApi("error.password.noval")))
    case p => Valid
  }

}









