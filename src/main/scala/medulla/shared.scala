package medulla

object login {

  import org.scalajs.dom.{console, document}
  import com.raquo.airstream.core.EventStream

  trait LoginHelper {
    def isLoggedIn: Boolean
    def test(in: Boolean, user: Option[UserToken]): EventStream[Option[UserToken]]
  }

  abstract class DefaultLoginHelper extends LoginHelper {

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
      console.debug(s"[Medulla] Testing cookie:$in, user:${user.map(_.email).getOrElse("_")}")
      if (in) retrieve else EventStream.fromValue(None)
    }

    def retrieve: EventStream[Option[UserToken]]
  }
}

object render {

  import com.raquo.laminar.api.L.*

  trait AppRender {
    def whenLoggedIn(user: UserToken): Signal[HtmlElement]
    def whenLoggedOut                : Signal[HtmlElement]
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