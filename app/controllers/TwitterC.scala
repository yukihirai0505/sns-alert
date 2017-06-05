package controllers

import javax.inject._

import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.Json
import play.api.mvc._

import com.danielasfregola.twitter4s.TwitterRestClient
import models.TwitterUserInfo
import services.InstagramService

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
  * Created by yukihirai on 2017/03/18.
  */
@Singleton
class TwitterC @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, cache: CacheApi)
  extends InstagramService(dbConfigProvider, env, cache) {

  def getFollowerCount(name: String) = Action.async { implicit req: Request[_] =>
    try {
      val restClient = TwitterRestClient()
      val result = Await.result(restClient.user(screen_name = name), Duration.Inf)
      Future successful Ok(Json.toJson(TwitterUserInfo(result.data.followers_count, result.data.`protected`)))
    } catch {
      case e: Exception => Future successful InternalServerError("{\"error\": \"error occured\"}")
    }
  }
}
