package medulla.ui.views

import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.*
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

abstract class LoadingView[T] {

  def load               (using ExecutionContext): EventStream[Try[T]]
  def renderItem(item: T)(using ExecutionContext): HtmlElement

  def renderResult(maybe: Option[Try[T]])(using ExecutionContext) = {
    maybe match
      case Some(Success(item: T)) => renderItem(item)
      case Some(Failure(err))     => div(s"Error: ${err.getMessage}")
      case None                   => div("Loading")
  }

  def render(using ExecutionContext) = {

    val item = Var(Option.empty[Try[T]])

    div(
      cls("contents"),
      load.map(Some(_)) --> item,
      child <-- item.signal.map(renderResult)
    )
  }
}
