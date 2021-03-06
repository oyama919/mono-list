import com.typesafe.config.{ Config, ConfigFactory }
import scala.collection.JavaConverters._

name := """mono-list"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.7"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

libraryDependencies ++= Seq(
  "org.scalikejdbc"        %% "scalikejdbc"                     % "3.2.3",
  "org.scalikejdbc"        %% "scalikejdbc-config"              % "3.2.3",
  "org.scalikejdbc"        %% "scalikejdbc-jsr310"              % "2.5.2",
  "org.scalikejdbc"        %% "scalikejdbc-test"                % "3.2.3" % Test,
  "org.scalikejdbc"        %% "scalikejdbc-syntax-support-macro"% "3.2.3",
  "org.scalikejdbc"        %% "scalikejdbc-play-initializer"    % "2.6.+",
  "org.skinny-framework"   %% "skinny-orm"                      % "2.3.7",
  "ch.qos.logback"         % "logback-classic"                  % "1.2.3",
  "com.adrianhurt"         %% "play-bootstrap"                  % "1.2-P26-B3",
  "com.github.t3hnar"      %% "scala-bcrypt"                    % "3.1",
  "com.github.j5ik2o"      %% "scala-rakuten-item-search-api"   % "1.0.3",
  "jp.t2v"                 %% "play2-auth"                      % "0.16.0-SNAPSHOT",
  "jp.t2v"                 %% "play2-auth-test"                 % "0.16.0-SNAPSHOT" % Test,
  "mysql"                  % "mysql-connector-java"             % "6.0.6",
  "org.flywaydb"           %% "flyway-play"                     % "4.0.0"
)

lazy val envConfig = settingKey[Config]("env-config")

envConfig := {
  val env = sys.props.getOrElse("env", "dev")
  ConfigFactory.parseFile(file("env") / (env + ".conf"))
}

flywayLocations := envConfig.value.getStringList("flywayLocations").asScala
flywayDriver := envConfig.value.getString("jdbcDriver")
flywayUrl := envConfig.value.getString("jdbcUrl")
flywayUser := envConfig.value.getString("jdbcUserName")
flywayPassword := envConfig.value.getString("jdbcPassword")

TwirlKeys.templateImports ++= Seq("forms._")
