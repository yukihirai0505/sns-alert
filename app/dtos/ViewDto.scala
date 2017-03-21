package dtos

/**
  * author Yuki Hirai on 2017/03/21.
  */
object ViewDto {

  case class ViewDto(
                      headTagInfo: HeadTagInfo
                    )

  case class HeadTagInfo(
                          title: String = "SNS-ALERT"
                          , description: String = "TODO"
                          , keywords: String = "SNS,ALERT,アラート,SNSアラート,SNS-ALERT"
                        )
}