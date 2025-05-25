package medulla

import com.raquo.airstream.core.EventStream
import com.raquo.laminar.api.L.*
import com.raquo.waypoint.SplitRender
import medulla.Pages.*
import medulla.login.{DefaultLoginHelper, LoginHelper}
import medulla.render.AppRender
import org.scalajs.dom.*

class SampleLoginHelper extends DefaultLoginHelper {
  override def retrieve = EventStream.fromValue(Some(new UserToken {
    override def name = "Leandro Cruz"
    override def email = "leandro@medulla.com"
  }))
}

object Pages {
  sealed trait Page
  case object HomePage                 extends Page
  case object TestPage                 extends Page
  case class  UnknownPage(str: String) extends Page
}

class DefaultRouter {

}

object MedullaRouter {

  import com.raquo.laminar.api.L
  import com.raquo.laminar.api.L.*
  import com.raquo.waypoint.*
  import io.circe.*
  import io.circe.generic.auto.*
  import io.circe.parser.*
  import io.circe.syntax.*
  import medulla.Pages.*

  private def title        (page: Page)   : String  = "Console Medulla"
  private def encodePage   (page: Page)   : String  = page.asJson.spaces2SortKeys
  private def decodePage   (data: String) : Page    = decode[Page](data).getOrElse(UnknownPage(data))
  private def unknownRoute (str: String)  : Page    = UnknownPage(str)

  object HomePageView {
    def route = Route.static(HomePage, root / "home")
  }

  object TestPageView {
    def route = Route.static(TestPage, root / "test")
  }

  val routes = List(
    TestPageView.route,
    HomePageView.route,
  )

  private val router = Router[Page](routes, encodePage, decodePage, title, unknownRoute)

  def page: StrictSignal[Page] = router.currentPageSignal
}

class SampleAppRender extends AppRender {

  override def whenLoggedOut = Signal.fromValue(div("Logged Out!"))

  override def whenLoggedIn(user: UserToken) = SplitRender[Page, HtmlElement](MedullaRouter.page).collect {
    case TestPage => div("test")
    case HomePage => div("home")
    case UnknownPage(p) => div(
      b(s"Hello ${user.name}!"),
      br(),
      small(s"(page: $p)")
    )
  }.signal
}


