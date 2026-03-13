package medulla.material

import com.raquo.laminar.api.L.*
import com.raquo.laminar.codecs.*

/** Material Design 3 Checkbox.
  *
  * Requires importing in JS:
  * {{{
  * import '@material/web/checkbox/checkbox.js'
  * }}}
  */
object MdCheckbox:

  private val tag = htmlTag("md-checkbox")

  val checked       : HtmlAttr[Boolean] = htmlAttr("checked", BooleanAsAttrPresenceCodec)
  val indeterminate : HtmlAttr[Boolean] = htmlAttr("indeterminate", BooleanAsAttrPresenceCodec)
  val required      : HtmlAttr[Boolean] = htmlAttr("required", BooleanAsAttrPresenceCodec)

  def apply(modifiers: Modifier[HtmlElement]*) = tag(modifiers*)
