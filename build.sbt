name := "world-watch"

organization := "com.taintech"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.1"

crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.1")

resolvers += "Dukascopy" at "https://www.dukascopy.com/client/jforexlib/publicrepo/"

libraryDependencies ++= Seq(
  "com.dukascopy.dds2" % "DDS2-jClient-JForex" % "3.0.18",
  "com.dukascopy.api" % "JForex-API" % "2.13.30",
  "com.typesafe" % "config" % "1.3.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
)

initialCommands := "import com.taintech.worldwatch._"
