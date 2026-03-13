package medulla.material

import com.raquo.laminar.api.L.*
import com.raquo.laminar.codecs.*
import org.scalajs.dom

/** Material Design 3 Tabs.
  *
  * Requires importing in JS:
  * {{{
  * import '@material/web/tabs/tabs.js'
  * import '@material/web/tabs/primary-tab.js'
  * import '@material/web/tabs/secondary-tab.js'
  * }}}
  */
object MdTabs:
  private val tag = htmlTag("md-tabs")

  val autoActivate: HtmlAttr[Boolean]  = htmlAttr("auto-activate", BooleanAsAttrPresenceCodec)
  val activeTabIndex: HtmlAttr[Int]    = htmlAttr("active-tab-index", IntAsStringCodec)

  val onchange: EventProp[dom.Event] = eventProp("change")

  def apply(modifiers: Modifier[HtmlElement]*) = tag(modifiers*)

object MdTab:

  val inlineIcon: HtmlAttr[Boolean] = htmlAttr("inline-icon", BooleanAsAttrPresenceCodec)
  val isTab: HtmlAttr[Boolean]      = htmlAttr("md-tab", BooleanAsAttrPresenceCodec)
  val active: HtmlAttr[Boolean]     = htmlAttr("active", BooleanAsAttrPresenceCodec)

  def primary(modifiers: Modifier[HtmlElement]*)   = htmlTag("md-primary-tab")(modifiers*)
  def secondary(modifiers: Modifier[HtmlElement]*) = htmlTag("md-secondary-tab")(modifiers*)
