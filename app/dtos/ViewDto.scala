package dtos

import play.api.data.Form

import forms.LoginForms.LoginForm
import forms.VerifyMailForms.VerifyMailForm
import models.BaseEntity
import models.Entities.AccountEntity

/**
  * author Yuki Hirai on 2017/03/21.
  */
object ViewDto {

  case class ViewDto(
                      account: Option[AccountEntity] = None
                      , headTagInfo: HeadTagInfo
                      , loginForm: Option[Form[LoginForm]] = None
                      , pageEntity: Option[BaseEntity] = None
                      , verifyMailForm: Option[Form[VerifyMailForm]] = None
                      , instagramAuthUrl: Option[String] = None
                    )

  case class HeadTagInfo(
                          title: String = "SNS-ALERT"
                          , description: String = "TODO"
                          , keywords: String = "SNS,ALERT,アラート,SNSアラート,SNS-ALERT"
                        )

}