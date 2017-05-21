name := """sns-alert"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
//  .aggregate(sFacebook)
//  .dependsOn(sFacebook)
// Facebook
//lazy val sFacebook = uri("git://github.com/yukihirai0505/sFacebook.git#5b209f4ad7f7ff4def692be90b0d715c73b12f98")

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val slickJodaMapperV = "2.2.0"
  val jodaTimeV = "2.9.3"
  val jodaConvertV = "1.8.1"
  Seq(
    cache,
    ws,
    filters,
    // Akka
    "com.enragedginger" %% "akka-quartz-scheduler" % "1.6.0-akka-2.4.x",

    // Instagram
    "com.yukihirai0505" % "sinstagram_2.11" % "0.0.7",
    // Facebook
    "com.yukihirai0505" % "sfacebook_2.11" % "0.0.2",
    // Twitter
    "com.danielasfregola" %% "twitter4s" % "5.0",

    // DB
    "com.typesafe.play" %% "play-slick" % "2.0.0",
    "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
    "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
    "com.github.tototoshi" % "slick-joda-mapper_2.11" % slickJodaMapperV,
    "joda-time" % "joda-time" % jodaTimeV,
    "org.joda" % "joda-convert" % jodaConvertV,

    // Mail
    "com.typesafe.play" %% "play-mailer" % "5.0.0",

    // TEST
    "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
  )
}