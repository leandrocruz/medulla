package medulla.ui.modal

import com.raquo.laminar.api.L.*
import medulla.ui.overlay.Overlay

object Modal {
  def apply(content: HtmlElement, opened: Var[Boolean] = Var(true)) = {

    def render(show: Boolean): Option[HtmlElement] = {
      if(show) Some(Overlay(div(cls("fixed top-0 right-0 h-full min-w-96 bg-white"), content)))
      else     None
    }

    opened.signal.map(render)
  }
}