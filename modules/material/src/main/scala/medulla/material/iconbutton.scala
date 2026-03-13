package medulla.material

import com.raquo.laminar.api.L.*
import com.raquo.laminar.codecs.*

/** Material Design 3 Icon Button components.
  *
  * Requires importing in JS:
  * {{{
  * import '@material/web/iconbutton/icon-button.js'
  * import '@material/web/iconbutton/filled-icon-button.js'
  * import '@material/web/iconbutton/filled-tonal-icon-button.js'
  * import '@material/web/iconbutton/outlined-icon-button.js'
  * }}}
  */
object MdIconButton:

  val toggle: HtmlAttr[Boolean] = htmlAttr("toggle", BooleanAsAttrPresenceCodec)

  def apply(modifiers: Modifier[HtmlElement]*)       = htmlTag("md-icon-button")(modifiers*)
  def filled(modifiers: Modifier[HtmlElement]*)      = htmlTag("md-filled-icon-button")(modifiers*)
  def filledTonal(modifiers: Modifier[HtmlElement]*) = htmlTag("md-filled-tonal-icon-button")(modifiers*)
  def outlined(modifiers: Modifier[HtmlElement]*)    = htmlTag("md-outlined-icon-button")(modifiers*)
