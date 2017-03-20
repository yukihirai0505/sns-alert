name := """code-generater"""
version := "1.0"
scalaVersion := "2.11.7"

libraryDependencies ++= {
    val slickV = "3.1.1"
    Seq(
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "com.jsuereth" %% "scala-arm" % "1.4",
    "com.typesafe" % "config" % "1.3.0",
    "com.typesafe.slick" % "slick_2.11" % slickV,
    "com.typesafe.slick" % "slick-codegen_2.11" % slickV,
    "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"
    )
}

