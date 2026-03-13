package medulla.material

import com.raquo.laminar.api.L.*
import com.raquo.laminar.codecs.*

/** Material Design 3 Switch.
  *
  * Requires importing in JS:
  * {{{
  * import '@material/web/switch/switch.js'
  * }}}
  */
object MdSwitch:

  private val tag = htmlTag("md-switch")

  val icons                : HtmlAttr[Boolean] = htmlAttr("icons"                  , BooleanAsAttrPresenceCodec)
  val showOnlySelectedIcon : HtmlAttr[Boolean] = htmlAttr("show-only-selected-icon", BooleanAsAttrPresenceCodec)
  val required             : HtmlAttr[Boolean] = htmlAttr("required"               , BooleanAsAttrPresenceCodec)

  def apply(modifiers: Modifier[HtmlElement]*) = tag(modifiers*)
