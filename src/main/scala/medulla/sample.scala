package medulla.sample

import medulla.ui.SimpleGridLayout

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
    def apply(): HtmlElement = h1("HOME")
  }

  object TestPageView {
    def apply(): HtmlElement = {
      val value = Var("")
      div(
        cls("flex flex-col"),
        div("Value: ", child.text <-- value),
        input(
          cls("p-2 my-4 rounded-md"),
          typ("text"),
          onInput.mapToValue --> value
        ),
      )
    }
  }

  object UnknownPageView {
    def apply(s: String): HtmlElement = div(s"Unknown page for '$s'")
  }

  class SampleAppRender extends AppRender[Long] {

    override def whenLoggedOut = div("Logged Out!")

    override def whenLoggedIn(user: UserToken[Long]) = {

      val main = router.render {
        case SampleRouter.HomePage => HomePageView()
        case SampleRouter.TestPage => TestPageView()
        case UnknownPage(str)      => UnknownPageView(str)
      }

      val left = Signal.fromValue {
        div(
          div(a(router.linkTo(HomePage), "home")),
          div(a(router.linkTo(TestPage), "test"))
        )
      }

      SimpleGridLayout(
        Signal.fromValue(div(cls("m-auto font-bold italic text-xl"), "Medulla")),
        Signal.fromValue(h1(s"${user.email} (${user.id})")),
        left,
        main
      ).render
    }
  }
}


