import org.scalajs.linker.interface.{ESVersion, ModuleSplitStyle}

ThisBuild / scalaVersion := "3.7.2"
ThisBuild / organization := "leandrocruz"
ThisBuild / version      := "v0.0.1-SNAPSHOT"

val sharedSettings = Seq(
  libraryDependencies ++= Seq(
    "org.wvlet.airframe" %%% "airframe"             % "23.5.6",
    "com.raquo"          %%% "laminar"              % "17.2.0",
    "com.raquo"          %%% "waypoint"             % "9.0.0",
    "io.circe"           %%% "circe-core"           % "0.14.5",
    "io.circe"           %%% "circe-parser"         % "0.14.5",
    "io.circe"           %%% "circe-generic"        % "0.14.5",
    "io.github.cquiroz"  %%% "scala-java-time"      % "2.5.0",
    "io.github.cquiroz"  %%% "scala-java-time-tzdb" % "2.5.0",
    "org.scalatest"      %%% "scalatest"            % "3.2.18" % Test
  )
)

lazy val root = (project in file("."))
  .settings(name := "medulla")
  .aggregate(framework, sample)

lazy val framework = project.in(file("modules/framework"))
  .enablePlugins(ScalaJSPlugin) // Enable the Scala.js plugin in this project
  .settings(
    name := "medulla-framework",
    scalaJSLinkerConfig ~= {
      _ .withESFeatures(_.withESVersion(ESVersion.ES2018))
        .withModuleKind(ModuleKind.ESModule)
        .withSourceMap(true)
        .withModuleSplitStyle(ModuleSplitStyle.SmallModulesFor(List("medulla")))
    },
    sharedSettings
  )


lazy val sample = project.in(file("modules/sample"))
  .enablePlugins(ScalaJSPlugin) // Enable the Scala.js plugin in this project
  .settings(
    name := "medulla-sample",
    scalaJSUseMainModuleInitializer := true,
    sharedSettings
  )
  .dependsOn(framework)