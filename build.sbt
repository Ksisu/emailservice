lazy val commonSettings = Seq(
  scalaVersion := "2.12.8",
  organization := "com.wanari",
  scalafmtOnCompile := true
)

lazy val root = project
  .in(file("."))
  .aggregate(core)

lazy val core = (project in file("."))
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
      val akkaHttpV = "10.1.8"
      val akkaV     = "2.5.22"
      Seq(
        "org.typelevel"        %% "cats-core"               % "1.6.0",
        "com.typesafe.akka"    %% "akka-http"               % akkaHttpV,
        "com.typesafe.akka"    %% "akka-http-spray-json"    % akkaHttpV,
        "com.typesafe.akka"    %% "akka-http-testkit"       % akkaHttpV % "test",
        "com.typesafe.akka"    %% "akka-actor"              % akkaV,
        "com.typesafe.akka"    %% "akka-stream"             % akkaV,
        "com.typesafe.akka"    %% "akka-slf4j"              % akkaV,
        "com.typesafe.akka"    %% "akka-testkit"            % akkaV % "test",
        "ch.qos.logback"       % "logback-classic"          % "1.2.3",
        "net.logstash.logback" % "logstash-logback-encoder" % "5.3",
        "org.slf4j"            % "jul-to-slf4j"             % "1.7.26",
        "org.codehaus.janino"  % "janino"                   % "3.0.12",
        "org.apache.commons"   % "commons-email"            % "1.5",
        "org.scalatra.scalate" % "scalate-core_2.12"        % "1.9.3",
        "io.opentracing"       % "opentracing-api"          % "0.32.0",
        "io.opentracing"       % "opentracing-util"         % "0.32.0",
        "io.opentracing"       % "opentracing-noop"         % "0.32.0",
        "io.jaegertracing"     % "jaeger-client"            % "0.34.0",
        "org.scalatest"        %% "scalatest"               % "3.0.7" % "test",
        "org.mockito"          % "mockito-core"             % "2.27.0" % "test",
        "org.mockito"          %% "mockito-scala"           % "1.3.1" % "test"
      )
    }
  )

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")

enablePlugins(JavaAppPackaging)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.10")
addCompilerPlugin("io.tryp"        % "splain"          % "0.4.0" cross CrossVersion.patch)

cancelable in Global := true
