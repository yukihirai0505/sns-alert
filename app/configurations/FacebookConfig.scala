package configurations

import controllers.routes
import play.api.mvc.{Call, RequestHeader}
import play.api.{Environment, Mode}

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
  lazy val AUTH_URL: (RequestHeader, Environment) => String = (req: RequestHeader, env: Environment) => {
    s"https://www.facebook.com/dialog/oauth?client_id=$ID&redirect_uri=${REDIRECT_URL(req, env)}&scope=public_profile"
  }
}
