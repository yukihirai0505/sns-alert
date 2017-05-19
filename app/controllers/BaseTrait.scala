package controllers

import play.api.mvc.RequestHeader

import dtos.ViewDto.{HeadTagInfo, ViewDto}
import models.Entities.AccountEntity

/**
  * Created by yukihirai on 2017/03/20.
  */
trait BaseTrait {

  def createViewDto(req: RequestHeader, account: AccountEntity, headTagInfo: HeadTagInfo): ViewDto = {
    ViewDto(
      account = Some(account)
      , headTagInfo = headTagInfo
    )
  }

}
