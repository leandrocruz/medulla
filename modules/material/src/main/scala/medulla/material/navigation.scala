package medulla.material

import com.raquo.laminar.api.L.*
import com.raquo.laminar.codecs.*

/** Material Design 3 Navigation components.
  *
  * Requires importing in JS:
  * {{{
  * import '@material/web/navigationbar/navigation-bar.js'
  * import '@material/web/navigationtab/navigation-tab.js'
  * import '@material/web/navigationdrawer/navigation-drawer.js'
  * import '@material/web/navigationrail/navigation-rail.js' // (if available)
  * }}}
  */
object MdNavigationBar:
  private val tag = htmlTag("md-navigation-bar")

  val activeIndex: HtmlAttr[Int] = htmlAttr("active-index", IntAsStringCodec)

  def apply(modifiers: Modifier[HtmlElement]*) = tag(modifiers*)

object MdNavigationTab:
  private val tag = htmlTag("md-navigation-tab")

  val active: HtmlAttr[Boolean]     = htmlAttr("active", BooleanAsAttrPresenceCodec)
  val badgeValue: HtmlAttr[String]  = htmlAttr("badge-value", StringAsIsCodec)
  val showBadge: HtmlAttr[Boolean]  = htmlAttr("show-badge", BooleanAsAttrPresenceCodec)

  def apply(modifiers: Modifier[HtmlElement]*) = tag(modifiers*)

object MdNavigationDrawer:
  private val tag = htmlTag("md-navigation-drawer")

  val opened: HtmlAttr[Boolean] = htmlAttr("opened", BooleanAsAttrPresenceCodec)

  def apply(modifiers: Modifier[HtmlElement]*) = tag(modifiers*)
