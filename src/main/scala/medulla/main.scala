package medulla

@main
def main(): Unit = {

  import medulla.config.MedullaConfig
  import wvlet.airframe.*
  import medulla.render.AppRender
  import medulla.login.LoginHelper
  import com.raquo.laminar.api.L.renderOnDomContentLoaded
  import org.scalajs.dom.document

  newDesign
    .bind[MedullaConfig].toInstance(config.localDev)
    .bind[LoginHelper]  .to[SampleLoginHelper]
    .bind[AppRender]    .to[SampleAppRender]
    .bind[MedullaApp]   .to[DefaultMedullaApp]
    .build[MedullaApp] { app =>
      lazy val container = document.getElementById("app") // must be lazy
      renderOnDomContentLoaded(container, app.rootElement)
    }
}