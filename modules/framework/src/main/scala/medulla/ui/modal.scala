package medulla.ui.modal

import com.raquo.laminar.api.L.*
import medulla.ui.overlay.Overlay

object Modal {
  def apply(opened: Var[Boolean], content: Signal[HtmlElement]) = {
    opened.signal.map {
      Option.when(_) {
        Overlay(div(cls("fixed top-0 right-0 h-full min-w-96 bg-white"), child <-- content))
      }
    }
  }
}