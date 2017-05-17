package configurations


import com.yukihirai0505.sInstagram.InstagramAuth
import com.yukihirai0505.sInstagram.model.{ResponseType, Scope}
import com.yukihirai0505.sInstagram.responses.auth.Auth
import controllers.routes
import dispatch.Future
import play.api.{Environment, Mode}
import play.api.mvc.{Call, RequestHeader}

/**
  * Created by yukihirai on 2017/03/20.
  */
trait InstagramConfig extends Config {
  private lazy val instagramConfig = config.getConfig("instagram")
  lazy val INSTAGRAM_ID: String = instagramConfig.getString("client.id")
  lazy val INSTAGRAM_SECRET: String = instagramConfig.getString("client.secret")
  private lazy val SCOPES: Seq[Scope] = Seq(Scope.BASIC, Scope.PUBLIC_CONTENT)
  private lazy val CALL_BACK_URL = (req: RequestHeader, env: Environment) =>
    Call("GET", routes.InstagramC.callback("").absoluteURL(secure = env.mode.equals(Mode.Prod))(req)).url
  val AUTH_URL: (RequestHeader, Environment) => String = (req: RequestHeader, env: Environment) =>
    (new InstagramAuth).authURL(clientId = INSTAGRAM_ID, callbackUrl = CALL_BACK_URL(req, env), responseType = ResponseType.CODE, scopes = SCOPES)
  val ACCESS_TOKEN: (RequestHeader, String, Environment) => Future[Option[Auth]] = (req: RequestHeader, code: String, env: Environment) =>
    (new InstagramAuth).requestToken(INSTAGRAM_ID, INSTAGRAM_SECRET, callbackUrl = CALL_BACK_URL(req, env), code = code)
}
