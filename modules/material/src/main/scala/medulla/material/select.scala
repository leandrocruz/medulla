package medulla.material

import com.raquo.laminar.api.L.*
import com.raquo.laminar.codecs.*
import org.scalajs.dom

/** Material Design 3 Select menus.
  *
  * Requires importing in JS:
  * {{{
  * import '@material/web/select/filled-select.js'
  * import '@material/web/select/outlined-select.js'
  * import '@material/web/select/select-option.js'
  * }}}
  */
object MdSelect:

  val quick: HtmlAttr[Boolean]       = htmlAttr("quick", BooleanAsAttrPresenceCodec)
  val required: HtmlAttr[Boolean]    = htmlAttr("required", BooleanAsAttrPresenceCodec)
  val error: HtmlAttr[Boolean]       = htmlAttr("error", BooleanAsAttrPresenceCodec)
  val errorText: HtmlAttr[String]    = htmlAttr("error-text", StringAsIsCodec)
  val menuPositioning: HtmlAttr[String] = htmlAttr("menu-positioning", StringAsIsCodec) // "absolute", "fixed", "popover"

  def filled(modifiers: Modifier[HtmlElement]*)   = htmlTag("md-filled-select")(modifiers*)
  def outlined(modifiers: Modifier[HtmlElement]*) = htmlTag("md-outlined-select")(modifiers*)

object MdSelectOption:
  private val tag = htmlTag("md-select-option")
  def apply(modifiers: Modifier[HtmlElement]*) = tag(modifiers*)
