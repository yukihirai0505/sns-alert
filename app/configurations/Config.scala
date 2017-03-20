package configurations

/**
  * Created by yukihirai on 2017/03/20.
  */
import com.typesafe.config.ConfigFactory

trait Config {
  def config = LoadedConfig.config
}

object LoadedConfig {
  lazy val config = ConfigFactory.load
}