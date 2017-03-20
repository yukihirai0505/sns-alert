package configurations

import com.yukihirai0505.Authentication
import com.yukihirai0505.http.Response
import com.yukihirai0505.model.{ResponseType, Scope}
import com.yukihirai0505.responses.auth.Auth
import controllers.routes
import dispatch.Future
import play.api.{Environment, Mode}
import play.api.mvc.{Call, RequestHeader}

/**
  * Created by yukihirai on 2017/03/20.
  */
trait InstagramConfig extends Config {
  private lazy val instagramConfig = config.getConfig("instagram.client")
  private lazy val CLIENT_ID = instagramConfig.getString("id")
  private lazy val SCOPES: Seq[Scope] = Seq(Scope.BASIC)
  private lazy val CALL_BACK_URL = (req: RequestHeader, env: Environment) => Call("GET", routes.InstagramC.callback("").absoluteURL(secure = env.mode.equals(Mode.Prod))(req)).url
  private lazy val SECRET = instagramConfig.getString("secret")
  val AUTH_URL: (RequestHeader, Environment) => String = (req: RequestHeader, env: Environment) =>
    (new Authentication).authURL(CLIENT_ID, CALL_BACK_URL(req, env), ResponseType.CODE, SCOPES)
  val ACCESS_TOKEN: (RequestHeader, String, Environment) => Future[Response[Auth]] = (req: RequestHeader, code: String, env: Environment) =>
    (new Authentication).requestToken(CLIENT_ID, SECRET, CALL_BACK_URL(req, env), code)
}