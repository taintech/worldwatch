name := "world-watch"

organization := "com.taintech"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.1"

crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.1")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
)

initialCommands := "import com.taintech.worldwatch._"
