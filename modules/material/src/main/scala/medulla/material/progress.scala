package medulla.material

import com.raquo.laminar.api.L.*
import com.raquo.laminar.codecs.*

/** Material Design 3 Progress indicators.
  *
  * Requires importing in JS:
  * {{{
  * import '@material/web/progress/linear-progress.js'
  * import '@material/web/progress/circular-progress.js'
  * }}}
  */
object MdProgress:

  val indeterminate: HtmlAttr[Boolean] = htmlAttr("indeterminate", BooleanAsAttrPresenceCodec)
  val fourColor: HtmlAttr[Boolean]     = htmlAttr("four-color", BooleanAsAttrPresenceCodec)

  // value and max are set as attributes (0 to 1 for value, defaults max=1)
  val progressValue: HtmlAttr[String] = htmlAttr("value", StringAsIsCodec)
  val max: HtmlAttr[String]           = htmlAttr("max", StringAsIsCodec)
  val buffer: HtmlAttr[String]        = htmlAttr("buffer", StringAsIsCodec)

  def linear(modifiers: Modifier[HtmlElement]*)   = htmlTag("md-linear-progress")(modifiers*)
  def circular(modifiers: Modifier[HtmlElement]*) = htmlTag("md-circular-progress")(modifiers*)
