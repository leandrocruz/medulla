package medulla.ui.overlay

import com.raquo.laminar.api.L.*

object Overlay {
  def apply(child: HtmlElement) = {
    div(
      cls("fixed inset-0 bg-black bg-opacity-50 z-50"), div(cls("z-51"), child)
    )
  }
}