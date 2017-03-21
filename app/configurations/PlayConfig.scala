package configurations

/**
  * Created by yukihirai on 2017/03/20.
  */
trait PlayConfig extends Config {
  private lazy val playConfig = config.getConfig("play")
  val CRYPTO_SECRET: String = playConfig.getString("crypto.secret")
}
