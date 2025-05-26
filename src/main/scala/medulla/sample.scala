package medulla

import com.raquo.airstream.core.EventStream
import com.raquo.laminar.api.L.*
import com.raquo.waypoint.*
import io.circe.*
import io.circe.generic.semiauto.*
import medulla.login.DefaultLoginHelper
import medulla.render.AppRender
import medulla.router.MedullaRouter
import org.scalajs.dom.*

class SampleLoginHelper extends DefaultLoginHelper {
  override def retrieve = EventStream.fromValue {
    Some {
      new UserToken {
        override def name  = "Leandro Cruz"
        override def email = "leandro@medulla.com"
      }
    }
  }
}

class SampleAppRender extends AppRender {

  sealed trait Page
  case object HomePage                extends Page
  case object TestPage                extends Page
  case class UnknownPage(str: String) extends Page

  given Decoder[Page] = deriveDecoder
  given Encoder[Page] = deriveEncoder

  private val router = new MedullaRouter[Page](UnknownPage.apply) (
    Route.static(TestPage, root / "test"),
    Route.static(HomePage, root / "home")
  )

  override def whenLoggedOut = div("Logged Out!")

  override def whenLoggedIn(user: UserToken) = {
    div(
      h1(s"TOP ${user.email}"),
      child <-- router.render {
        case HomePage       => div("HOME")
        case TestPage       => div("TEST")
        case UnknownPage(s) => div(s"TODO: ${s}")
      }
    )
  }
}


