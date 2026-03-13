package medulla.material

import com.raquo.laminar.api.L.*
import com.raquo.laminar.codecs.*

/** Material Design 3 Floating Action Button.
  *
  * Requires importing in JS:
  * {{{
  * import '@material/web/fab/fab.js'
  * import '@material/web/fab/branded-fab.js'
  * }}}
  */
object MdFab:

  val size: HtmlAttr[String]      = htmlAttr("size", StringAsIsCodec) // "small", "medium", "large"
  val lowered: HtmlAttr[Boolean]  = htmlAttr("lowered", BooleanAsAttrPresenceCodec)
  val variant: HtmlAttr[String]   = htmlAttr("variant", StringAsIsCodec) // "surface", "primary", "secondary", "tertiary"

  def apply(modifiers: Modifier[HtmlElement]*)   = htmlTag("md-fab")(modifiers*)
  def branded(modifiers: Modifier[HtmlElement]*) = htmlTag("md-branded-fab")(modifiers*)
