package configurations

import play.api.mvc.{Call, RequestHeader}
import play.api.{Environment, Mode}

import com.yukihirai0505.sFacebook.FacebookAuth
import com.yukihirai0505.sFacebook.model.Scope
import com.yukihirai0505.sFacebook.responses.auth.Oauth
import controllers.routes
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
  private lazy val facebookAuth = new FacebookAuth
  private lazy val scopes: Seq[Scope] = Seq(Scope.PUBLIC_PROFILE, Scope.PUBLISH_ACTIONS, Scope.USER_POSTS)
  lazy val AUTH_URL: (RequestHeader, Environment) => String = (req: RequestHeader, env: Environment) => {
    facebookAuth.authURL(ID, REDIRECT_URL(req, env), scopes)
  }
  lazy val ACCESS_TOKEN: (String, RequestHeader, Environment) => Future[Option[Oauth]] = (code: String, req: RequestHeader, env: Environment) => {
    facebookAuth.requestToken(ID, SECRET, REDIRECT_URL(req, env), code)
  }
}
