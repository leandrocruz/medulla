package medulla.sample

object login {

  import medulla.UserToken
  import medulla.login.DefaultLoginHelper
  import com.raquo.airstream.core.EventStream

  class SampleLoginHelper extends DefaultLoginHelper {
    override def retrieve = EventStream.fromValue {
      Some {
        new UserToken[Long] {
          override def id    = 1
          override def name  = "Leandro Cruz"
          override def email = "leandro@medulla.com"
        }
      }
    }
  }
}

object SampleRouter {

  import medulla.router.MedullaRouter
  import com.raquo.waypoint.*
  import io.circe.*
  import io.circe.generic.semiauto.*

  sealed trait Page
  case object HomePage                extends Page
  case object TestPage                extends Page
  case class UnknownPage(str: String) extends Page

  given Decoder[Page] = deriveDecoder
  given Encoder[Page] = deriveEncoder

  val router = new MedullaRouter[Page](UnknownPage.apply) (
    Route.static(TestPage, root / "test"),
    Route.static(HomePage, root / "home")
  )
}

object render {

  import SampleRouter.*
  import medulla.render.AppRender
  import medulla.UserToken
  import com.raquo.laminar.api.L.*
  import org.scalajs.dom.*

  object HomePageView {
    def apply(): HtmlElement = div("HOME")
  }

  object TestPageView {
    def apply(): HtmlElement = div("TEST")
  }

  object UnknownPageView {
    def apply(s: String): HtmlElement = {
      div(
        div(s"TODO: ${s}"),
        div(a(router.linkTo(HomePage), "HOME")),
        div(a(router.linkTo(TestPage), "TEST"))
      )
    }
  }

  object SimpleLayout {
    import medulla.router.MedullaRouter

    def apply(router: MedullaRouter[Page], user: UserToken[Long]): HtmlElement = {
      div(
        h1(s"TOP ${user.email} (${user.id})"),
        child <-- router.render {
          case HomePage       => HomePageView()
          case TestPage       => TestPageView()
          case UnknownPage(s) => UnknownPageView(s)
        }
      )

    }
  }

  class SampleAppRender extends AppRender[Long] {
    override def whenLoggedOut                       = div("Logged Out!")
    override def whenLoggedIn(user: UserToken[Long]) = SimpleLayout(router, user)
  }
}


