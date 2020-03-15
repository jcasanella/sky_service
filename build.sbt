lazy val akkaHttpVersion = "10.1.11"
lazy val akkaVersion    = "2.6.3"

lazy val nexusUrl = settingKey[String]("Check if it runs in Jenkins")

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.casanella.service",
      version         := "0.1-SNAPSHOT",
      scalaVersion    := "2.13.1",
      nexusUrl := {
        (sys.env.get("USERNAME")) match {
          case Some(username) =>
            println(s"Do my thing $username")
            "localhost"
          case _ =>
            println("USERNAME is missing " + Path.userHome.toString)
            "172.17.0.3"
        }
      }
    )),
    name := "sky_service",
    resolvers ++= Seq(
      MavenRepository("Nexus Local Docker", s"http://${nexusUrl.value}:8081/repository/maven-releases/")
    ),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "ch.qos.logback"    % "logback-classic"           % "1.2.3",

      "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"                % "3.0.8"         % Test
    ),
    publishTo := {
      val nexus = s"http://${nexusUrl.value}:8081/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "repository/maven-snapshots/")
      else
        Some("releases"  at nexus + "repository/maven-releases/")
    }
  )
