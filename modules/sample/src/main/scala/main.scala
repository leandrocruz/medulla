package medulla.sample

@main
def main(): Unit = {

  import medulla.{MedullaApp, DefaultMedullaApp}
  import medulla.config.MedullaConfig
  import medulla.render.AppRender
  import medulla.login.LoginHelper
  import medulla.sample.login.SampleLoginHelper
  import medulla.sample.render.SampleAppRender
  import wvlet.airframe.*
  import com.raquo.laminar.api.L.renderOnDomContentLoaded
  import org.scalajs.dom.document

  type UID = Long

  newDesign
    .bind [MedullaConfig   ].toInstance(medulla.config.localDev)
    .bind [LoginHelper[UID]].to[SampleLoginHelper]
    .bind [AppRender  [UID]].to[SampleAppRender]
    .bind [MedullaApp      ].to[DefaultMedullaApp[UID]]
    .build[MedullaApp] { app =>
      lazy val container = document.getElementById("app") // must be lazy
      renderOnDomContentLoaded(container, app.rootElement)
    }
}