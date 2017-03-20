import slick.codegen.SourceCodeGenerator
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.model.{Model, QualifiedName}
import slick.profile.RelationalProfile.ColumnOption.Default
import slick.profile.SqlProfile.ColumnOption.SqlType

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}




/**
  * Main
  * ../app/models/Tables.scala
  */
object SnsAlertDB extends App {
  val slickDriver = "slick.driver.PostgresDriver"
  val jdbcDriver = "org.postgresql.Driver"
  val url = "jdbc:postgresql://127.0.0.1:5432/sns_alert"

  val outputFolder = "../app"

  val outputFileName = "Tables"
  val schemas = "models"
  val pkg = "models"
  val user = "root"
  val password = "root"
  val driver: JdbcProfile = slick.driver.PostgresDriver
  val db = { Database.forURL(url, driver = jdbcDriver, user = user, password = password) }

  val model = Await.result(db.run(driver.createModel(None, ignoreInvalidDefaults = false)(ExecutionContext.global).withPinnedSession), Duration.Inf)

  // Remove create_date and update_date without Tables.scala
  val ts = for {
    t <- model.tables.filter(_.name.table != "play_evolutions")
    c = t.columns.filter(_.name != "create_date").filter(_.name != "update_date")
  } yield {
    val cc = c.head.table match {
      case QualifiedName(x, _, _) =>
        for(a <- c) yield {
          if (a.name == "private") {a.copy(nullable = true, options = Set(SqlType("BIT"), Default(Some(false))))}
          else a
        }
      case _ => c
    }
    slick.model.Table(t.name, cc, t.primaryKey, ArrayBuffer(), ArrayBuffer(), t.options)
  }
  val fModel = Model(tables = ts)
  val codeGenFuture = new CustomGenerator(fModel).writeToFile(slickDriver, outputFolder , pkg, outputFileName, s"$outputFileName.scala")
}
