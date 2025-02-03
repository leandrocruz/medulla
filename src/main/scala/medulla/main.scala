package medulla

import medulla.config.MedullaConfig

@main
def main(): Unit = {

  import wvlet.airframe.*
  import medulla.render.{AppRender, StaticAppRender}
  import medulla.login.{LoginHelper, StaticLoginHelper}
  import com.raquo.laminar.api.L.renderOnDomContentLoaded
  import org.scalajs.dom.document

  newDesign
    .bind[MedullaConfig].toInstance(config.dev)
    .bind[LoginHelper]  .to[StaticLoginHelper]
    .bind[AppRender]    .to[StaticAppRender]
    .bind[MedullaApp]   .to[DefaultMedullaApp]
    .build[MedullaApp] { app =>
      lazy val container = document.getElementById("app") // must be lazy
      renderOnDomContentLoaded(container, app.rootElement)
    }
}