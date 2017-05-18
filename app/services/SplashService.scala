package services

import javax.inject.Inject

import controllers.BaseTrait
import daos.{SplashPostDAO, UserDAO}
import dtos.ViewDto.{HeadTagInfo, ViewDto}
import models.Entities.{AccountEntity, SplashEntity}
import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.{Controller, Request, RequestHeader}

import utils.SessionUtil

import scala.concurrent.ExecutionContext.Implicits.global
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
    val _account = account(req)
    val v = viewDto(req, "Splash")
    if (_account.isLogin)
      new SplashPostDAO(dbConfigProvider).getByUserId(_account.user.get.id.get).flatMap { posts =>
        val pageEntity = SplashEntity(splashPosts = Some(posts))
        Future successful Right(v.copy(pageEntity = Some(pageEntity)))
      }
    else Future successful Left(v)
  }

}

