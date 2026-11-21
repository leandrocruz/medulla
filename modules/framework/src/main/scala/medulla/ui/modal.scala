package medulla.ui.modal

import com.raquo.laminar.api.L.*
import medulla.ui.overlay.Overlay

object Modal {
  def apply(opened: Signal[Boolean], content: Signal[HtmlElement], clazz: String = "min-w-96") = {
    opened.map {
      Option.when(_) {
        Overlay(div(cls(s"fixed top-0 right-0 h-full bg-white $clazz"), child <-- content))
      }
    }
  }
}