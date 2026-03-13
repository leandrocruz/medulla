package medulla.material

import com.raquo.laminar.api.L.*
import com.raquo.laminar.codecs.*

/** Material Design 3 List.
  *
  * Requires importing in JS:
  * {{{
  * import '@material/web/list/list.js'
  * import '@material/web/list/list-item.js'
  * }}}
  */
object MdList:
  private val tag = htmlTag("md-list")
  def apply(modifiers: Modifier[HtmlElement]*) = tag(modifiers*)

object MdListItem:
  private val tag = htmlTag("md-list-item")

  // "text", "button", "link"
  val listItemType: HtmlAttr[String] = htmlAttr("type", StringAsIsCodec)

  def apply(modifiers: Modifier[HtmlElement]*) = tag(modifiers*)
