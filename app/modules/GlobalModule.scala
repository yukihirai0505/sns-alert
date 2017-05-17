package modules

import play.api.Logger
import play.api.inject.ApplicationLifecycle

import actors.AlertMail
import akka.actor.{ActorSystem, Props}
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
class GlobalSetting @Inject()(lifecycle: ApplicationLifecycle){
  // TODO: delete facebook post automatically
  /***
  Logger.info("Start application...")
  val system = ActorSystem("AlertMail")
  val actor = system.actorOf(Props(classOf[AlertMail]))
  QuartzSchedulerExtension(system).schedule("AlertMailEveryHour", actor, "メール送信")
  lifecycle.addStopHook { () =>
    Future.successful(null)
  }***/
}
