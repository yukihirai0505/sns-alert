package configurations

import com.yukihirai0505.Authentication
import com.yukihirai0505.model.{ResponseType, Scope}
import controllers.routes
import play.api.mvc.{Call, RequestHeader}

/**
  * Created by yukihirai on 2017/03/20.
  */
trait InstagramConfig extends Config {
  private lazy val instagramConfig = config.getConfig("instagram.client")
  private lazy val CLIENT_ID = instagramConfig.getString("id")
  private lazy val SCOPES: Seq[Scope] = Seq(Scope.BASIC)
  private lazy val CALL_BACK_URL = (req: RequestHeader) => Call("GET", routes.InstagramC.callback().absoluteURL()(req)).url
  val SECRET = instagramConfig.getString("secret")
  val authUrl = (req: RequestHeader) => (new Authentication).authURL(CLIENT_ID, CALL_BACK_URL(req), ResponseType.CODE, SCOPES)
}
