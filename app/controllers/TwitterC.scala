package controllers

import javax.inject._

import com.danielasfregola.twitter4s.TwitterRestClient
import play.api.Environment
import play.api.cache.CacheApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc._
import services.InstagramService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
  * Created by yukihirai on 2017/03/18.
  */
@Singleton
class TwitterC @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment, cache: CacheApi)
  extends InstagramService(dbConfigProvider, env, cache)
{

  def getFollowerCount(name: String) = Action.async { implicit req: Request[_] =>
    try {
      val restClient = TwitterRestClient()
      val result = Await.result(restClient.user(screen_name = name), Duration.Inf)
      Future successful Ok("{ \"follower_count\": " + result.data.followers_count + " }")
    } catch {
      case e: Exception => Future successful InternalServerError("{\"error\": \"error occured\"}")
    }
  }
}
