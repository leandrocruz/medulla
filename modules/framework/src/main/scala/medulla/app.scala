package medulla

import com.raquo.laminar.api.L.*
import medulla.config.MedullaConfig
import medulla.login.LoginHelper
import medulla.render.AppRender
import org.scalajs.dom.*

trait MedullaApp {
  def rootElement: HtmlElement
}

case class DefaultMedullaApp[UID](config: MedullaConfig, login: LoginHelper[UID], render: AppRender[UID]) extends MedullaApp {

  override def rootElement: HtmlElement = {

    def cast(user: UserToken[_]) = user.asInstanceOf[UserToken[UID]]

    def renderBody(maybe: Option[UserToken[UID]]): HtmlElement = {
      maybe match
        case Some(user) => render.whenLoggedIn(user)
        case None       => render.whenLoggedOut
    }

    val user: EventStream[Option[UserToken[UID]]] = EventStream
      .periodic(config.login.testCookieEvery)
      .map(_ => login.isLoggedIn)
      .distinct
      .withCurrentValueOf(Globals.user)
      .map( (a, b) => (a, b.map(cast)) )
      .flatMapSwitch(login.test)

    div(
      display("contents"), //https://caniuse.com/css-display-contents
      user  --> Globals.userUpdates,
      child <-- Globals.user.map(_.map(cast)).map(renderBody)
    )
  }
}