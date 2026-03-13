package medulla.material

import com.raquo.laminar.api.L.*
import com.raquo.laminar.codecs.*
import org.scalajs.dom

/** Material Design 3 Chips.
  *
  * Requires importing in JS:
  * {{{
  * import '@material/web/chips/chip-set.js'
  * import '@material/web/chips/assist-chip.js'
  * import '@material/web/chips/filter-chip.js'
  * import '@material/web/chips/input-chip.js'
  * import '@material/web/chips/suggestion-chip.js'
  * }}}
  */
object MdChipSet:
  private val tag = htmlTag("md-chip-set")
  def apply(modifiers: Modifier[HtmlElement]*) = tag(modifiers*)

object MdChip:

  val elevated: HtmlAttr[Boolean]  = htmlAttr("elevated", BooleanAsAttrPresenceCodec)
  val removable: HtmlAttr[Boolean] = htmlAttr("removable", BooleanAsAttrPresenceCodec)

  val onRemove: EventProp[dom.Event] = eventProp("remove")

  def assist(modifiers: Modifier[HtmlElement]*)     = htmlTag("md-assist-chip")(modifiers*)
  def filter(modifiers: Modifier[HtmlElement]*)     = htmlTag("md-filter-chip")(modifiers*)
  def input(modifiers: Modifier[HtmlElement]*)      = htmlTag("md-input-chip")(modifiers*)
  def suggestion(modifiers: Modifier[HtmlElement]*) = htmlTag("md-suggestion-chip")(modifiers*)
