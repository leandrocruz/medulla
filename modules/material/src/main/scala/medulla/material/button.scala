package medulla.material

import com.raquo.laminar.api.L.*
import com.raquo.laminar.codecs.*

/** Material Design 3 Button components.
  *
  * Requires importing in JS:
  * {{{
  * import '@material/web/button/filled-button.js'
  * import '@material/web/button/outlined-button.js'
  * import '@material/web/button/text-button.js'
  * import '@material/web/button/elevated-button.js'
  * import '@material/web/button/filled-tonal-button.js'
  * }}}
  */
object MdButton:

  val trailingIcon: HtmlAttr[Boolean] = htmlAttr("trailing-icon", BooleanAsAttrPresenceCodec)

  def filled     (modifiers: Modifier[HtmlElement]*) = htmlTag("md-filled-button")      (modifiers*)
  def outlined   (modifiers: Modifier[HtmlElement]*) = htmlTag("md-outlined-button")    (modifiers*)
  def text       (modifiers: Modifier[HtmlElement]*) = htmlTag("md-text-button")        (modifiers*)
  def elevated   (modifiers: Modifier[HtmlElement]*) = htmlTag("md-elevated-button")    (modifiers*)
  def filledTonal(modifiers: Modifier[HtmlElement]*) = htmlTag("md-filled-tonal-button")(modifiers*)
