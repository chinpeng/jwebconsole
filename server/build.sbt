name := "jwebconsole-server"

version := "0.0.1"

scalaVersion := "2.10.1"

resolvers += "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"

libraryDependencies ++= Seq(
  "org.eclipse.jetty" % "jetty-server" % "9.1.0.v20131115",
  "org.eclipse.jetty" % "jetty-servlet" % "9.1.0.v20131115"
)

libraryDependencies ++= Seq(
  "org.scalatra" % "scalatra_2.10" % "2.3.0.M1" excludeAll(ExclusionRule(organization = "org.slf4j")),
  "org.scalatra" % "scalatra-json_2.10" % "2.3.0.M1",
  "org.scalatra" % "scalatra-scalate_2.10" % "2.3.0.M1",
  "org.json4s"   %% "json4s-jackson" % "3.2.6"
)

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.13"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3-M2",
  "com.typesafe.akka" %% "akka-persistence-experimental" % "2.3-M2",
  "com.typesafe.akka" % "akka-slf4j_2.10" % "2.3-M2"
)

libraryDependencies ++= Seq(
  "org.specs2" % "specs2_2.10" % "2.3.7" % "test",
  "org.mockito" % "mockito-all" % "1.9.5" % "test",
  "junit" % "junit" % "4.11" % "test"
)


