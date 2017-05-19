package configurations

import com.typesafe.config.ConfigFactory

/**
  * Created by yukihirai on 2017/03/20.
  */
trait Config {
  def config = LoadedConfig.config
}

object LoadedConfig {
  lazy val config = ConfigFactory.load
}