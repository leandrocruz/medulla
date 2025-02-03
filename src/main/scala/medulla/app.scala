package medulla

import com.raquo.laminar.api.L.*
import medulla.login.LoginHelper
import medulla.render.AppRender
import com.raquo.waypoint.SplitRender
import medulla.Pages.Page
import medulla.config.MedullaConfig
import org.scalajs.dom.*

trait MedullaApp {
  def rootElement: HtmlElement
}

case class DefaultMedullaApp(config: MedullaConfig, login: LoginHelper, render: AppRender) extends MedullaApp {

  override def rootElement: HtmlElement = {

    def renderBody(body: Signal[HtmlElement])(maybe: Option[UserToken]): HtmlElement = {
      maybe match
        case Some(user) => render.whenLoggedIn(user)
        case None       => render.whenLoggedOut
    }

    val body: SplitRender[Page, HtmlElement] = SplitRender[Page, HtmlElement](MedullaRouter.page).collect {
      case p => div(s"Unknown Page '$p'}")
    }

    val user: EventStream[Option[UserToken]] = EventStream
      .periodic(config.login.testCookieEvery)
      .map(_ => login.isLoggedIn)
      .distinct
      .withCurrentValueOf(Globals.currentUser)
      .flatMapSwitch(login.test)

    div(
      user  --> Globals.user,
      child <-- Globals.user.events.distinct.map(renderBody(body.signal))
    )
  }
}