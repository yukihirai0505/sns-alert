package configurations

import controllers.routes
import play.api.mvc.{Call, RequestHeader}
import play.api.{Environment, Mode}
import com.yukihirai0505.sFacebook.Authentication
import com.yukihirai0505.sFacebook.http.Response
import com.yukihirai0505.sFacebook.model.Scope
import com.yukihirai0505.sFacebook.auth.Auth
import dispatch.Future

/**
  * Created by yukihirai on 2017/03/20.
  */
trait FacebookConfig extends Config {
  private lazy val facebookConfig = config.getConfig("facebook")
  lazy val ID: String = facebookConfig.getString("id")
  lazy val SECRET: String = facebookConfig.getString("secret")
  lazy val REDIRECT_URL: (RequestHeader, Environment) => String = (req: RequestHeader, env: Environment) => {
    val url = Call("GET", routes.FacebookC.callback("").absoluteURL(secure = env.mode.equals(Mode.Prod))(req)).url
    url.replace("?code=", "")
  }
  private lazy val authentication = new Authentication
  private lazy val scopes: Seq[Scope] = Seq(Scope.PUBLIC_PROFILE)
  lazy val AUTH_URL: (RequestHeader, Environment) => String = (req: RequestHeader, env: Environment) => {
    authentication.authURL(ID, REDIRECT_URL(req, env), scopes)
  }
  lazy val ACCESS_TOKEN: (String, RequestHeader, Environment) => Future[Response[Auth]] = (code: String, req: RequestHeader, env: Environment) => {
    authentication.requestToken(ID, SECRET, REDIRECT_URL(req, env), code)
  }
}
