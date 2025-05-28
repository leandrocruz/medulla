package medulla

@main
def main(): Unit = {

  import medulla.config.MedullaConfig
  import wvlet.airframe.*
  import medulla.render.AppRender
  import medulla.login.LoginHelper
  import com.raquo.laminar.api.L.renderOnDomContentLoaded
  import org.scalajs.dom.document

  type UID = Long

  newDesign
    .bind [MedullaConfig   ].toInstance(config.localDev)
    .bind [LoginHelper[UID]].to[SampleLoginHelper]
    .bind [AppRender  [UID]].to[SampleAppRender]
    .bind [MedullaApp      ].to[DefaultMedullaApp[UID]]
    .build[MedullaApp] { app =>
      lazy val container = document.getElementById("app") // must be lazy
      renderOnDomContentLoaded(container, app.rootElement)
    }
}