package medulla

object Pages {
  sealed trait Page
  case object HomePage                 extends Page
  case object TestPage                 extends Page
  case class  UnknownPage(str: String) extends Page
}

object MedullaRouter {

  import com.raquo.laminar.api.L
  import com.raquo.laminar.api.L.*
  import com.raquo.waypoint.*
  import medulla.Pages.*
  import com.raquo.waypoint.Router
  import io.circe.*
  import io.circe.syntax.*
  import io.circe.generic.auto.*
  import io.circe.parser.*

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

object login {

  import org.scalajs.dom.console
  import com.raquo.airstream.core.EventStream
  import medulla.cookie.Cookie
  import org.scalajs.dom.document

  trait LoginHelper {
    def isLoggedIn: Boolean
    def test(in: Boolean, user: Option[UserToken]): EventStream[Option[UserToken]]
  }

  class StaticLoginHelper extends LoginHelper {

    override def isLoggedIn = {
      medulla
        .cookie
        .Cookie
        .parse(document.cookie)
        .exists { (_, cookie) =>
          cookie.name == "medulla-auth-loggedin" && cookie.value == "true"
        }
    }

    override def test(in: Boolean, user: Option[UserToken]) = {
      console.info(s"[Medulla] Testing cookie:$in, user:${user.map(_.email).getOrElse("_")}")

      if(!in) EventStream.fromValue(None)
      else    EventStream.fromValue(Some(new UserToken {
        override def name = "Leandro Cruz"
        override def email = "leandro@medulla.com"
      }))
    }
  }
}

object render {

  import medulla.Pages.*
  import org.scalajs.dom.*
  import com.raquo.laminar.api.L.*
  import com.raquo.waypoint.SplitRender

  trait AppRender {
    def whenLoggedIn(user: UserToken) : Signal[HtmlElement]
    def whenLoggedOut                 : Signal[HtmlElement]
  }

  class StaticAppRender extends AppRender {

    override def whenLoggedOut = Signal.fromValue(div("Logged Out!"))

    override def whenLoggedIn(user: UserToken) = SplitRender[Page, HtmlElement](MedullaRouter.page).collect {
      case TestPage       => div("test")
      case HomePage       => div("home")
      case UnknownPage(p) => div(
        b(s"Hello ${user.name}!"),
        br(),
        small(s"(page: $p)")
      )
    }.signal
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

object cookie {

  import scala.scalajs.js.URIUtils.decodeURIComponent

  case class Cookie(
    name     : String,
    value    : String,
    path     : Option[String] = None,
    domain   : Option[String] = None,
    expires  : Option[String] = None,
    maxAge   : Option[Int]    = None,
    secure   : Boolean        = false,
    httpOnly : Boolean        = false,
    sameSite : Option[String] = None
  )

  object Cookie {
    def parse(cookieString: String): Map[String, Cookie] = {
      cookieString
        .split(";\\s*")
        .flatMap { _.split("=", 2) match
          case Array(rawName, rawValue) =>
            val name  = decodeURIComponent(rawName.trim)
            val value = decodeURIComponent(rawValue.trim)
            Some(name -> Cookie(name, value))
          case _ => None
        }.toMap
    }
  }
}