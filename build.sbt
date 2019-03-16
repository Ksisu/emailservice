import org.scalafmt.sbt.ScalafmtPlugin.scalafmtConfigSettings

lazy val commonSettings = Seq(
  scalaVersion := "2.12.8",
  organization := "com.wanari",
  scalafmtOnCompile := true
)

lazy val ItTest         = config("it") extend Test
lazy val itTestSettings = Defaults.itSettings ++ scalafmtConfigSettings

lazy val root = project
  .in(file("."))
  .aggregate(core)

lazy val core = (project in file("."))
  .configs(ItTest)
  .settings(inConfig(ItTest)(itTestSettings): _*)
  .settings(commonSettings: _*)
  .settings(
    name := "emailservice",
    version := "0.1.0",
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding",
      "UTF-8",
      "-unchecked",
      "-feature",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-language:postfixOps",
      "-Ypartial-unification",
      "-Ywarn-dead-code",
      "-Xlint"
    ),
    libraryDependencies ++= {
      val akkaHttpV = "10.1.7"
      val akkaV     = "2.5.21"
      Seq(
        "org.typelevel"        %% "cats-core"               % "1.6.0",
        "com.typesafe.akka"    %% "akka-http"               % akkaHttpV,
        "com.typesafe.akka"    %% "akka-http-spray-json"    % akkaHttpV,
        "com.typesafe.akka"    %% "akka-http-testkit"       % akkaHttpV % "it,test",
        "com.typesafe.akka"    %% "akka-actor"              % akkaV,
        "com.typesafe.akka"    %% "akka-stream"             % akkaV,
        "com.typesafe.akka"    %% "akka-slf4j"              % akkaV,
        "com.typesafe.akka"    %% "akka-testkit"            % akkaV % "it,test",
        "ch.qos.logback"       % "logback-classic"          % "1.2.3",
        "net.logstash.logback" % "logstash-logback-encoder" % "5.3",
        "org.slf4j"            % "jul-to-slf4j"             % "1.7.26",
        "org.codehaus.janino"  % "janino"                   % "3.0.12",
        "org.scalatest"        %% "scalatest"               % "3.0.5" % "it,test",
        "org.mockito"          % "mockito-core"             % "2.25.0" % "it,test",
        "org.mockito"          %% "mockito-scala"           % "1.2.0" % "it,test"
      )
    }
  )

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt it:scalafmt")
addCommandAlias("testAll", "; test ; it:test")

enablePlugins(JavaAppPackaging)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.9")
addCompilerPlugin("io.tryp"        % "splain"          % "0.4.0" cross CrossVersion.patch)

cancelable in Global := true
