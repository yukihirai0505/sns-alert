name := """sns-alert"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  cache,
  ws,
  filters,

  // Instagram
  "com.yukihirai0505" % "sinstagram_2.11" % "0.0.3",
  // Twitter
  "com.danielasfregola" %% "twitter4s" % "5.0",

  // DB
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",

  // Mail
  "com.typesafe.play" %% "play-mailer" % "5.0.0",

  // TEST
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

