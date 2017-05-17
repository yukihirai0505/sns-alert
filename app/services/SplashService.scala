package services

import javax.inject.Inject

import controllers.BaseTrait
import daos.UserDAO
import dtos.ViewDto.{HeadTagInfo, ViewDto}
import models.Entities.AccountEntity
import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.{Controller, Request, RequestHeader}
import utils.SessionUtil

import scala.concurrent.Future

/**
  * Created by yukihirai on 2017/03/20.
  */
class SplashService @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, implicit val cache: CacheApi)
  extends UserDAO(dbConfigProvider) with Controller with BaseTrait {

  val account: (RequestHeader) => AccountEntity = (req: RequestHeader) => SessionUtil.getAccount(req, cache)

  val viewDto: (RequestHeader, String) => ViewDto = (req: RequestHeader, headTitle: String) =>
    createViewDto(req, account(req), HeadTagInfo(title = headTitle))

  def getIndexViewDto(implicit req: Request[_]): Future[Either[ViewDto, ViewDto]] = {
    val v = viewDto(req, "Splash")
    Future successful {
      if (account(req).isLogin) {
        Right(v)
      } else Left(v)
    }
  }

}

