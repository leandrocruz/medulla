package medulla

object Pages {
  sealed trait Page
  case object HomePage                 extends Page
  case class  UnknownPage(str: String) extends Page
}

object MedullaRouter {

  import com.raquo.laminar.api.L
  import com.raquo.laminar.api.L.*
  import com.raquo.waypoint.*
  import org.scalajs.dom
  import org.scalajs.dom.console

  import medulla.Pages.*
  import com.raquo.waypoint.Router
  import io.circe.*
  import io.circe.syntax.*
  import io.circe.generic.auto.*
  import io.circe.parser.*

  private def title        (page: Page)   : String  = "Console Medulla"
  private def encodePage   (page: Page)   : String  = page.asJson.spaces2SortKeys
  private def decodePage   (data: String) : Page    = decode[Page](data).getOrElse(UnknownPage(data))
  private def unknownRoute (str: String)  : Page    = HomePage

  object HomePageView {
    def route = Route.static(HomePage, root)
  }

  val routes = List(
    HomePageView.route
  )

  private val router = Router[Page](routes, encodePage, decodePage, title)

  def page: StrictSignal[Page] = router.currentPageSignal
}

object login {

  import org.scalajs.dom.console
  import com.raquo.airstream.core.EventStream

  trait LoginHelper {
    def isLoggedIn: Boolean
    def test(in: Boolean, user: Option[UserToken]): EventStream[Option[UserToken]]
  }

  class StaticLoginHelper extends LoginHelper {

    override def isLoggedIn = true

    override def test(in: Boolean, user: Option[UserToken]) = {
      console.info(s"[Medulla] Testing cookie:$in, user:${user.nonEmpty}")
      EventStream.fromValue(Some(new UserToken {
        override def name = "Leandro Cruz"
        override def email = "leandro@medulla.com"
      }))
    }
  }
}

object render {

  import org.scalajs.dom.*
  import com.raquo.laminar.api.L.*

  trait AppRender {
    def whenLoggedIn(user: UserToken) : HtmlElement
    def whenLoggedOut                 : HtmlElement
  }

  class StaticAppRender extends AppRender {

    override def whenLoggedIn(user: UserToken) = b(s"Hello ${user.name}!")
    override def whenLoggedOut                 = div("Logged Out!")
  }
}

object tmp {
  trait A {
    def callB(): String
  }

  trait B {
    def str: String
  }

  class AImpl(b: B) extends A {
    override def callB() = b.str
  }

  class BImpl extends B {
    override def str = "I'm bbb"
  }
}

object config {

  case class FetchConfig(baseUrl: String)

  case class LoginConfig(testCookieEvery: Int)

  case class DefaultMedullaConfig(name: String, fetch: FetchConfig, login: LoginConfig) extends MedullaConfig

  trait MedullaConfig {
    def name  : String
    def fetch : FetchConfig
    def login : LoginConfig
  }

  val dev = DefaultMedullaConfig(
    name  = "Medulla Dev Mode",
    fetch = FetchConfig("http://localhost:9000"),
    login = LoginConfig(5000),
  )
}