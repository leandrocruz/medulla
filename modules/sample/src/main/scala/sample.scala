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
  case object BindValuePage           extends Page
  case object HomePage                extends Page
  case object ModalPage               extends Page
  case object DragAndDropPage         extends Page
  case class UnknownPage(str: String) extends Page

  given Decoder[Page] = deriveDecoder
  given Encoder[Page] = deriveEncoder

  val router = new MedullaRouter[Page](UnknownPage.apply) (
    Route.static(BindValuePage  , root / "bind" ),
    Route.static(ModalPage      , root / "modal"),
    Route.static(DragAndDropPage, root / "dnd"  ),
    Route.static(HomePage       , root / "home" )
  )
}

object render {

  import SampleRouter.*
  import medulla.ui.layout.SimpleGridLayout
  import medulla.ui.modal.Modal
  import medulla.ui.dnd.DragAndDrop
  import medulla.render.AppRender
  import medulla.UserToken
  import com.raquo.laminar.api.L.*
  import org.scalajs.dom.*

  object HomePageView {
    def apply(): HtmlElement = h1("HOME")
  }

  object BindValuePageView {
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

  object ModalPageView {
    def apply(): HtmlElement = {

      val opened = Var(false)
      val content = button("Hide", onClick.mapTo(false) --> opened)

      div(
        button(cls("p"), "Show", onClick.mapTo(true)  --> opened),
        child.maybe <-- Modal(content, opened)
      )
    }
  }

  object DragAndDropView {
    def apply(): HtmlElement = {
      DragAndDrop(
        div(
          cls("border p bg-white cursor-move"),
          "Drag Me!"
        )
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
        case BindValuePage    => BindValuePageView()
        case HomePage         => HomePageView()
        case ModalPage        => ModalPageView()
        case DragAndDropPage  => DragAndDropView()
        case UnknownPage(str) => UnknownPageView(str)
      }

      val left = Signal.fromValue {
        div(
          div(a(router.linkTo(HomePage)       , "home")),
          div(a(router.linkTo(BindValuePage)  , "bind")),
          div(a(router.linkTo(ModalPage)      , "modal")),
          div(a(router.linkTo(DragAndDropPage), "drag and drop"))
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


