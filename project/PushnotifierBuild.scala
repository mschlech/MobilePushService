import sbt._
import sbt.Keys._

object PushnotifierBuild extends Build {

  lazy val pushnotifier = Project(
    id = "pushnotifier",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "pushnotifier",
      organization := "com.entscheidungsbaum",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.0-RC1",
      resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
      resolvers += "Sonatype" at "https://oss.sonatype.org/content/repositories/releases",
      libraryDependencies ++= Seq("com.typesafe.akka" % "akka-actor_2.10.0-RC1" % "2.1.0-RC1",
        "org.scala-lang" % "scala-reflect" % "2.10.0-RC1",
        "com.typesafe.akka" % "akka-camel_2.10.0-RC1" % "2.1.0-RC1",
        "com.typesafe.akka" % "akka-testkit_2.10.0-RC1" % "2.1.0-RC1",
        "org.apache.activemq" % "activemq-core" % "5.4.1",
        "org.apache.activemq" % "activemq-camel" % "5.4.1",
        "org.apache.camel" % "camel-jetty" % "2.9.3" % "compile",
        "org.apache.camel" % "camel-core" % "2.10.0" exclude ("org.slf4j", "slf4j-api"),
        "org.scalatest" % "scalatest_2.10.0-RC1" % "2.0.M4-2.10.0-RC1-B1")))
}
