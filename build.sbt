import org.scalajs.linker.interface.{ESVersion, ModuleSplitStyle}

lazy val medulla = project.in(file("."))
  .enablePlugins(ScalaJSPlugin) // Enable the Scala.js plugin in this project
  .settings(
    name := "medulla",
    scalaVersion := "3.3.1",

    // Tell Scala.js that this is an application with a main method
    scalaJSUseMainModuleInitializer := true,

    /* Configure Scala.js to emit modules in the optimal way to
     * connect to Vite's incremental reload.
     * - emit ECMAScript modules
     * - emit as many small modules as possible for classes in the "livechart" package
     * - emit as few (large) modules as possible for all other classes
     *   (in particular, for the standard library)
     */
    scalaJSLinkerConfig ~= {
      _ .withESFeatures(_.withESVersion(ESVersion.ES2018))
        .withModuleKind(ModuleKind.ESModule)
        .withSourceMap(true)
        .withModuleSplitStyle(ModuleSplitStyle.SmallModulesFor(List("medulla")))
    },

    /* Depend on the scalajs-dom library.
     * It provides static types for the browser DOM APIs.
     */
    libraryDependencies ++= List(
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
