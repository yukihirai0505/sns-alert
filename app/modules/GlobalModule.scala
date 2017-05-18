package modules

import play.api.{Application, Environment, Logger, Mode}
import play.api.inject.ApplicationLifecycle
import actors.{AlertMail, SplashPost}
import akka.actor.{ActorRef, ActorSystem, Props}
import com.google.inject.{AbstractModule, Inject}
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension

import scala.concurrent.Future

/**
  * author Yuki Hirai on 2017/03/22.
  */
class GlobalModule extends AbstractModule {
  override def configure() = {
    bind(classOf[GlobalSetting]).asEagerSingleton()
  }
}

class GlobalSetting @Inject()(lifecycle: ApplicationLifecycle, env: Environment) {
  Logger.info("Start application...")

  if (!env.mode.equals(Mode.Dev)) startActor

  lifecycle.addStopHook { () =>
    Future.successful(null)
  }

  def startActor = {
    val system: ActorSystem = ActorSystem("BatchSystem")
    val alertMailActor: ActorRef = system.actorOf(Props(classOf[AlertMail]))
    val splashPostActor: ActorRef = system.actorOf(Props(classOf[SplashPost]))

     // TODO: send sns search result mail function
    //QuartzSchedulerExtension(system).schedule("AlertMailEveryHour", alertMailActor, "send sns alert mail")

    QuartzSchedulerExtension(system).schedule("SplashPostEveryHour", splashPostActor, "splash posts")
  }

}