package medulla.ui

object Debug {

  import scala.util.{Success, Failure, Try, Either, Right, Left}
  import com.raquo.laminar.api.L.*

  extension [T <: AnyVal](value: T) {
    def debug(name: String) = div(s"$name = [$value]")
  }

  extension [T](maybe: Option[T]) {
    def debug(name: String) = {
      maybe match
        case None    => div(s"$name = _")
        case Some(v) => div(s"$name = [$v]")
    }
  }

  extension [T](tried: Try[T]) {
    def debug(name: String) = {
      tried match
        case Failure(e) => div(s"$name = error[${e.getMessage}]")
        case Success(s) => div(s"$name = success[$s]")
    }
  }

  extension [L, R](tried: Either[L, R]) {
    def debug(name: String) = {
      tried match
        case Left(l)  => div(s"$name = left[$l]")
        case Right(r) => div(s"$name = right[$r]")
    }
  }
}