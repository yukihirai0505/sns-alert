package configurations

/**
  * Created by yukihirai on 2017/03/20.
  */
trait InstagramConfig extends Config {
  private lazy val instagramConfig = config.getConfig("instagram.client")
  val CLIENT_ID = instagramConfig.getString("id")
  val SECRET = instagramConfig.getString("secret")
}
