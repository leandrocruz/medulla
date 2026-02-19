package medulla.fetch

import medulla.config.MedullaConfig
import org.scalajs.dom
import org.scalajs.dom.*
import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.*
import com.raquo.airstream.web.FetchOptions
import medulla.login.Logout
import medulla.{NoSpinner, Spinner}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

private type Opt = FetchOptions[dom.BodyInit] => Unit
type OptionSetter = HandlerOptions => HandlerOptions

class HandlerOptions {

  var spinner : Spinner = NoSpinner
  var log     : Log     = ConsoleLog()

  private def result(block: => Unit): HandlerOptions = {
    block
    this
  }

  def withSpinner(spinner: Spinner): HandlerOptions = result { this.spinner = spinner }
  def noLogging                    : HandlerOptions = result { this.log     = NoLog() }

  override def toString: String = s"spinner(${spinner}), log(${log})"
}

object HandlerOptions {
  def of(setters: Seq[OptionSetter]): HandlerOptions = {
    setters.foldLeft(HandlerOptions()) {
      (buffer, setter: OptionSetter) => {
        val result = setter(buffer)
        console.log(s"Applying ${setter} to ${buffer} => ${result}")
        result
      }
    }
  }
}

trait Log {
  def debug(msg: Any, other: Any*): Unit
  def info (msg: Any, other: Any*): Unit
  def error(msg: Any, other: Any*): Unit
}

class NoLog extends Log {
  override def debug(msg: Any, other: Any*) = ()
  override def info (msg: Any, other: Any*) = ()
  override def error(msg: Any, other: Any*) = ()
}

class ConsoleLog extends Log {
  override def debug(msg: Any, other: Any*) = dom.console.debug(msg, other: _*)
  override def info (msg: Any, other: Any*) = dom.console.info (msg, other: _*)
  override def error(msg: Any, other: Any*) = dom.console.error(msg, other: _*)
}

trait RequestEncoder[T] {
  def encode(i: T): Try[String]
}

trait ResponseDecoder[T] {
  def decode(text: String): Try[T]
}

trait Handler {

  def start[T](request: ValidRequest[T], options: HandlerOptions, url: String): Unit = {
    options.log.info(s"[Medulla] Fetch Start '${request.path}'")
    options.spinner.start
  }

  def stop[T](request: ValidRequest[T], options: HandlerOptions, response: Try[T]): Try[T] = {

    def onSuccess(value: T) = {
      options.log.info(s"[Medulla] Fetch Success '${request.path}'")
      options.spinner.stop
      Success(value)
    }

    def onError(e: Throwable) = {
      options.log.error(s"[Medulla] Fetch Failure '${request.path}': ${e.getMessage}")
      val refined = Exception(s"Error fetching url '${request.path}': ${e.getMessage}", e)
      options.spinner.stopWithError(refined)
      Failure(refined)
    }

    response match
      case Success(value)     => onSuccess(value)
      case Failure(exception) => onError(exception)
  }

  def handle[T](request: ValidRequest[T], options: HandlerOptions, response: Response)(using ExecutionContext): EventStream[Try[T]]
}

sealed trait RequestBuilder[T]

case class InvalidRequest[T](error: Throwable) extends RequestBuilder[T]

case class ValidRequest[T](
  method  : dom.HttpMethod,
  path    : String,
  options : Seq[Opt],
  dec     : ResponseDecoder[T]
) extends RequestBuilder[T]

case class UnifiedErrorFormat(
  origin  : String,
  message : String,
  trace   : Option[String]
)

private case class FetchRequestImpl(path: String, options: Seq[Opt]) {

  private def withBody[R, T](method: HttpMethod, body: R)(using enc: RequestEncoder[R], dec: ResponseDecoder[T]): RequestBuilder[T] = {
    enc.encode(body) match
      case Failure(err)  => InvalidRequest [T](err)
      case Success(text) => ValidRequest[T](method, path, options ++ Seq(_.headersAppend("Content-Type" -> "application/json"), _.body(text)), dec)
  }

  def noBody[T](method: HttpMethod)(using dec: ResponseDecoder[T]) = ValidRequest[T](HttpMethod.GET, path, options, dec)


  def get    [   T]         (using ResponseDecoder[T]                   ): RequestBuilder[T] = noBody  (HttpMethod.GET)
  def post   [R, T](body: R)(using ResponseDecoder[T], RequestEncoder[R]): RequestBuilder[T] = withBody(HttpMethod.POST  , body)
  def put    [R, T](body: R)(using ResponseDecoder[T], RequestEncoder[R]): RequestBuilder[T] = withBody(HttpMethod.PUT   , body)
  def delete [R, T](body: R)(using ResponseDecoder[T], RequestEncoder[R]): RequestBuilder[T] = withBody(HttpMethod.DELETE, body)
}

object FetchRequest {
  def apply(path: String, options: Seq[Opt] = Seq(_.credentials(_.include))) = FetchRequestImpl(path, options)
}

trait Fetcher {
  def execute[T](request: RequestBuilder[T], setters: OptionSetter*)(using ExecutionContext): EventStream[Try[T]]
}

case class BadFetcher(config: MedullaConfig) extends Fetcher {
  override def execute[T](request: RequestBuilder[T], setters: OptionSetter*)(using ExecutionContext) = {
    EventStream.fromValue(Failure(Exception("Can't execute request")))
  }
}

case class BasicFetcher(config: MedullaConfig, handler: Handler) extends Fetcher {
  override def execute[T](request: RequestBuilder[T], setters: OptionSetter*)(using ExecutionContext) = {

    def perform(it: ValidRequest[T]) = {
      val url     = config.fetch.baseUrl + it.path
      val options = HandlerOptions.of(setters)
      handler.start(it, options, url)
      FetchStream
        .raw(_ => it.method, url, it.options: _*)
        .flatMapSwitch { handler.handle(it, options, _) }
        .map           { handler.stop(it, options, _) }
        .recover {
          case err => Some(handler.stop(it, options, Failure(err)))
        }
    }

    request match
      case InvalidRequest(error) => EventStream.fromValue(Failure(error))
      case it: ValidRequest[T]   => perform(it)
  }
}

case class LogHandler(handler: Handler) extends Handler {
  override def handle[T](request: ValidRequest[T], options: HandlerOptions, response: Response)(using ExecutionContext) = {
    options.log.info(s"[Medulla] Fetch '${request.path}' handle status: ${response.status}")
    handler.handle(request, options, response)
  }
}

case class AuthAwareHandler[UID](handler: Handler, logout: Logout) extends Handler {
  override def handle[T](request: ValidRequest[T], options: HandlerOptions, response: Response)(using ExecutionContext) = {

    def onForbiddenLogOut: EventStream[Unit] = {
      options.log.error(s"[Medulla] Access Denied: ${response.status}")
      options.spinner.stopWithError(Exception("Access Denied"))
      logout.logout
    }

    response.status match
      case 401 | 403 => onForbiddenLogOut.map(_ => Failure(Exception("User Logged out")))
      case _         => handler.handle(request, options, response)

  }
}

case class JsonHandler() extends Handler {
  override def handle[T](request: ValidRequest[T], options: HandlerOptions, response: Response)(using ExecutionContext) = {

    def decode = EventStream.fromFuture {
      response
        .text()
        .toFuture
        .map(request.dec.decode)
        .recover { err => Failure(err) }
    }

    Option(response.headers.get("content-type")) match
      case Some(value) if value.startsWith("application/json") => decode
      case Some(other) => EventStream.fromValue(Failure(Exception(s"Response is not json: $other")))
      case _           => EventStream.fromValue(Failure(Exception("Response header 'content_type' is missing. Can't decode")))
  }
}