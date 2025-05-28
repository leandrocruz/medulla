package medulla

object login {

  import com.raquo.airstream.core.EventStream
  import org.scalajs.dom.{console, document}

  trait LoginHelper[UID] {
    def isLoggedIn: Boolean
    def test(in: Boolean, user: Option[UserToken[UID]]): EventStream[Option[UserToken[UID]]]
  }

  abstract class DefaultLoginHelper extends LoginHelper[Long] {

    override def isLoggedIn = {
      medulla
        .cookie
        .Cookie
        .parse(document.cookie)
        .exists { (_, cookie) =>
          cookie.name == "medulla-auth-loggedin" && cookie.value == "true"
        }
    }

    override def test(in: Boolean, user: Option[UserToken[Long]]) = {
      console.debug(s"[Medulla] Testing cookie:$in, user:${user.map(_.email).getOrElse("_")}")
      if (in) retrieve else EventStream.fromValue(None)
    }

    def retrieve: EventStream[Option[UserToken[Long]]]
  }
}

object render {

  import com.raquo.laminar.api.L.*

  trait AppRender[UID] {
    def whenLoggedIn(user: UserToken[UID]): HtmlElement
    def whenLoggedOut                     : HtmlElement
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

  val localDev = DefaultMedullaConfig(
    name  = "Medulla Local Dev Mode",
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

object router {

  import com.raquo.laminar.api.L
  import com.raquo.laminar.api.L.*
  import com.raquo.waypoint.{Route, Router, SplitRender}
  import io.circe.*
  import io.circe.parser.*
  import io.circe.syntax.*
  import scala.reflect.ClassTag
  import org.scalajs.dom
  import scala.util.{Try, Success, Failure}

  class MedullaRouter[BasePage: ClassTag](dftl: String => BasePage)(routes: Route[_ <: BasePage, _]*)(using Encoder[BasePage], Decoder[BasePage]) {

    protected def title        (page: BasePage) : String   = "Medulla"
    protected def encodePage   (page: BasePage) : String   = page.asJson.spaces2SortKeys
    protected def decodePage   (data: String)   : BasePage = decode[BasePage](data).getOrElse(dftl(data))
    protected def unknownRoute (str: String)    : BasePage = dftl(str)

    private val router = Router[BasePage](routes.toList, encodePage, decodePage, title, unknownRoute)

    def render(fn: BasePage => HtmlElement): Signal[HtmlElement] = SplitRender[BasePage, HtmlElement](router.currentPageSignal).collect(fn).signal

    def navigateTo(page: BasePage): Unit = router.pushState(page)

    def linkTo(page: BasePage): Binder[HtmlElement] = Binder { el =>
      val isLinkElement = el.ref.isInstanceOf[dom.html.Anchor]

      if (isLinkElement) {
        Try(router.absoluteUrlForPage(page)) match
          case Success(url) => el.amend(href(url))
          case Failure(err) => dom.console.error(s"Routing Error for '$page': " + err)
      }

      // If element is a link and user is holding a modifier while clicking:
      //  - Do nothing, browser will open the URL in new tab / window / etc. depending on the modifier key
      // Otherwise:
      //  - Perform regular pushState transition
      //  - Scroll to top of page

      val onRegularClick = onClick
        .filter(ev => !(isLinkElement && (ev.ctrlKey || ev.metaKey || ev.shiftKey || ev.altKey)))
        .preventDefault

      (onRegularClick --> { _ =>
        router.pushState(page)
        dom.window.scrollTo(0, 0) // Scroll to top of page when navigating
      }).bind(el)
    }


  }
}
