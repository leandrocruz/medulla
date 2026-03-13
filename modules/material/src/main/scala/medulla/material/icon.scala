package medulla.material

import com.raquo.laminar.api.L.*

/** Material Design 3 Icon.
  *
  * Requires importing in JS:
  * {{{
  * import '@material/web/icon/icon.js'
  * }}}
  *
  * Usage: `MdIcon("settings")`, `MdIcon("home")`
  */
object MdIcon:
  private val tag = htmlTag("md-icon")
  def apply(name: String, modifiers: Modifier[HtmlElement]*) = tag((Seq[Modifier[HtmlElement]](name) ++ modifiers)*)
