package controllers

import javax.inject._

import com.yukihirai0505.http.Response
import com.yukihirai0505.responses.auth.AccessToken
import configurations.InstagramConfig
import play.api.Environment
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc._
import services.InstagramService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by yukihirai on 2017/03/18.
  */
@Singleton
class InstagramC @Inject()(dbConfigProvider: DatabaseConfigProvider, env: Environment)
  extends InstagramService(dbConfigProvider)
    with InstagramConfig
{

  def callback(code: String) = Action.async { implicit req: Request[_] =>
    println(code)
    ACCESS_TOKEN(req, code, env) flatMap {
      case Response(Some(token: AccessToken), _, _) => Future successful Ok(token.token)
      case _ => Future successful BadRequest("An error")
    }
  }

}
