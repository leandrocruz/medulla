package medulla.material

import com.raquo.laminar.api.L.*
import com.raquo.laminar.codecs.*

/** Material Design 3 Radio button.
  *
  * Requires importing in JS:
  * {{{
  * import '@material/web/radio/radio.js'
  * }}}
  */
object MdRadio:

  private val tag = htmlTag("md-radio")

  val checked: HtmlAttr[Boolean]  = htmlAttr("checked", BooleanAsAttrPresenceCodec)
  val required: HtmlAttr[Boolean] = htmlAttr("required", BooleanAsAttrPresenceCodec)

  def apply(modifiers: Modifier[HtmlElement]*) = tag(modifiers*)
