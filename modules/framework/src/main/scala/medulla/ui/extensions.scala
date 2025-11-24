package medulla.ui.extensions

import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.*

object Extensions {
  extension (el: HtmlElement) {
    def border  = el.amend(cls("border"))
    def rounded = el.amend(cls("rounded"))
    def focused = el.amend(onMountFocus)
  }
}
