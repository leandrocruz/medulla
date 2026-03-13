package medulla.material

import com.raquo.laminar.api.L.*
import com.raquo.laminar.codecs.*
import org.scalajs.dom

/** Material Design 3 Dialog.
  *
  * Requires importing in JS:
  * {{{
  * import '@material/web/dialog/dialog.js'
  * }}}
  */
object MdDialog:

  private val tag = htmlTag("md-dialog")

  val open: HtmlAttr[Boolean] = htmlAttr("open", BooleanAsAttrPresenceCodec)

  val onOpen: EventProp[dom.Event]   = eventProp("open")
  val onOpened: EventProp[dom.Event] = eventProp("opened")
  val onClose: EventProp[dom.Event]  = eventProp("close")
  val onClosed: EventProp[dom.Event] = eventProp("closed")
  val onCancel: EventProp[dom.Event] = eventProp("cancel")

  def apply(modifiers: Modifier[HtmlElement]*) = tag(modifiers*)
