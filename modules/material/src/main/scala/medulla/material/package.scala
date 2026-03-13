package medulla.material

import com.raquo.laminar.api.L.*
import com.raquo.laminar.codecs.*
import com.raquo.laminar.keys.HtmlAttr
import org.scalajs.dom

/** Shared attributes and events used across Material Web components. */
object Md:

  // Common attributes
  val disabled: HtmlAttr[Boolean]  = htmlAttr("disabled", BooleanAsAttrPresenceCodec)
  val selected: HtmlAttr[Boolean]  = htmlAttr("selected", BooleanAsAttrPresenceCodec)
  val hasIcon: HtmlAttr[Boolean]   = htmlAttr("has-icon", BooleanAsAttrPresenceCodec)
  val label: HtmlAttr[String]      = htmlAttr("label", StringAsIsCodec)
  val headline: HtmlAttr[String]   = htmlAttr("headline", StringAsIsCodec)
  val supportingText: HtmlAttr[String] = htmlAttr("supporting-text", StringAsIsCodec)

  // Common events
  val onOpen: EventProp[dom.Event]   = eventProp("open")
  val onClose: EventProp[dom.Event]  = eventProp("close")
  val onChange: EventProp[dom.Event]  = eventProp("change")
  val onInput: EventProp[dom.Event]  = eventProp("input")
