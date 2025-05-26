package medulla

import com.raquo.laminar.api.L.*
import medulla.config.MedullaConfig
import medulla.login.LoginHelper
import medulla.render.AppRender
import org.scalajs.dom.*

trait MedullaApp {
  def rootElement: HtmlElement
}

case class DefaultMedullaApp(config: MedullaConfig, login: LoginHelper, render: AppRender) extends MedullaApp {

  override def rootElement: HtmlElement = {

    def renderBody(maybe: Option[UserToken]): HtmlElement = {
      maybe match
        case Some(user) => render.whenLoggedIn(user)
        case None       => render.whenLoggedOut
    }

    val user: EventStream[Option[UserToken]] = EventStream
      .periodic(config.login.testCookieEvery)
      .map(_ => login.isLoggedIn)
      .distinct
      .withCurrentValueOf(Globals.currentUser)
      .flatMapSwitch(login.test)

    div(
      display("contents"), //https://caniuse.com/css-display-contents
      user  --> Globals.user,
      child <-- Globals.user.events.distinct.map(renderBody)
    )
  }
}