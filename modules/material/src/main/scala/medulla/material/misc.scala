package medulla.material

import com.raquo.laminar.api.L.*
import com.raquo.laminar.codecs.*

/** Material Design 3 Ripple.
  *
  * Requires importing in JS: `import '@material/web/ripple/ripple.js'`
  */
object MdRipple:
  private val tag = htmlTag("md-ripple")
  def apply(modifiers: Modifier[HtmlElement]*) = tag(modifiers*)

/** Material Design 3 Elevation.
  *
  * Requires importing in JS: `import '@material/web/elevation/elevation.js'`
  */
object MdElevation:
  private val tag = htmlTag("md-elevation")
  def apply(modifiers: Modifier[HtmlElement]*) = tag(modifiers*)

/** Material Design 3 Divider.
  *
  * Requires importing in JS: `import '@material/web/divider/divider.js'`
  */
object MdDivider:
  private val tag = htmlTag("md-divider")

  val inset: HtmlAttr[Boolean]      = htmlAttr("inset", BooleanAsAttrPresenceCodec)
  val insetStart: HtmlAttr[Boolean] = htmlAttr("inset-start", BooleanAsAttrPresenceCodec)
  val insetEnd: HtmlAttr[Boolean]   = htmlAttr("inset-end", BooleanAsAttrPresenceCodec)

  def apply(modifiers: Modifier[HtmlElement]*) = tag(modifiers*)
