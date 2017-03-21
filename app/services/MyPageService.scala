package services

import javax.inject.Inject

import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Controller, Request}

import controllers.BaseTrait
import daos.UserDAO
import dtos.ViewDto.{HeadTagInfo, ViewDto}
import models.Entities.AccountEntity
import utils.SessionUtil

import scala.concurrent.Future

/**
  * Created by yukihirai on 2017/03/20.
  */
class MyPageService @Inject()(dbConfigProvider: DatabaseConfigProvider, implicit val cache: CacheApi, implicit val messagesApi: MessagesApi)
  extends UserDAO(dbConfigProvider) with Controller with BaseTrait with I18nSupport {

  def getMyPageViewDto(implicit req: Request[_]): Future[Either[ViewDto, ViewDto]] = {
    val account: AccountEntity = SessionUtil.getAccount
    val headTagInfo = HeadTagInfo(
      title = "MyPage"
    )
    val viewDto: ViewDto = createViewDto(req, account, headTagInfo).copy(account = Some(account))
    Future successful {
      if(account.isLogin) {
        Right(viewDto)
      }else Left(viewDto)
    }
  }

}