package services

import javax.inject.Inject

import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.i18n.MessagesApi
import play.api.mvc.{Controller, Request}

import configurations.InstagramConfig
import controllers.BaseTrait
import dtos.ViewDto.{HeadTagInfo, ViewDto}
import models.Entities.AccountEntity
import utils.SessionUtil

import scala.concurrent.Future

/**
  * Created by yukihirai on 2017/03/20.
  */
class TopService @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, implicit val cache: CacheApi, implicit val messagesApi: MessagesApi)
  extends Controller with InstagramConfig with BaseTrait {

  def getIndexViewDto(implicit req: Request[_]): Future[Either[ViewDto, ViewDto]] = {
    val account: AccountEntity = SessionUtil.getAccount
    val headTagInfo = HeadTagInfo(title = "Home")
    val viewDto: ViewDto = createViewDto(req, account, headTagInfo)
    Future.successful {
      if (account.isLogin) {
        Right(viewDto)
      } else Left(viewDto)
    }
  }
}

