package medulla.material

import com.raquo.laminar.api.L.*
import com.raquo.laminar.codecs.*
import org.scalajs.dom

/** Material Design 3 Menu.
  *
  * Requires importing in JS:
  * {{{
  * import '@material/web/menu/menu.js'
  * import '@material/web/menu/menu-item.js'
  * import '@material/web/menu/sub-menu.js'
  * }}}
  */
object MdMenu:
  private val tag = htmlTag("md-menu")

  val open: HtmlAttr[Boolean]          = htmlAttr("open", BooleanAsAttrPresenceCodec)
  val quick: HtmlAttr[Boolean]         = htmlAttr("quick", BooleanAsAttrPresenceCodec)
  val anchor: HtmlAttr[String]         = htmlAttr("anchor", StringAsIsCodec)
  val positioning: HtmlAttr[String]    = htmlAttr("positioning", StringAsIsCodec) // "absolute", "fixed", "popover"
  val hasOverflow: HtmlAttr[Boolean]   = htmlAttr("has-overflow", BooleanAsAttrPresenceCodec)

  val onOpening: EventProp[dom.Event]  = eventProp("opening")
  val onOpened: EventProp[dom.Event]   = eventProp("opened")
  val onClosing: EventProp[dom.Event]  = eventProp("closing")
  val onClosed: EventProp[dom.Event]   = eventProp("closed")

  def apply(modifiers: Modifier[HtmlElement]*) = tag(modifiers*)

object MdMenuItem:
  private val tag = htmlTag("md-menu-item")
  def apply(modifiers: Modifier[HtmlElement]*) = tag(modifiers*)

object MdSubMenu:
  private val tag = htmlTag("md-sub-menu")
  def apply(modifiers: Modifier[HtmlElement]*) = tag(modifiers*)
