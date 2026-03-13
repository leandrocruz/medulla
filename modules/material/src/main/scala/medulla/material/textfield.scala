package medulla.material

import com.raquo.laminar.api.L.*
import com.raquo.laminar.codecs.*
import org.scalajs.dom

/** Material Design 3 Text Field.
  *
  * Requires importing in JS:
  * {{{
  * import '@material/web/textfield/filled-text-field.js'
  * import '@material/web/textfield/outlined-text-field.js'
  * }}}
  */
object MdTextField:

  val error         : HtmlAttr[Boolean] = htmlAttr("error"      , BooleanAsAttrPresenceCodec)
  val errorText     : HtmlAttr[String]  = htmlAttr("error-text" , StringAsIsCodec)
  val prefixText    : HtmlAttr[String]  = htmlAttr("prefix-text", StringAsIsCodec)
  val suffixText    : HtmlAttr[String]  = htmlAttr("suffix-text", StringAsIsCodec)
  val required      : HtmlAttr[Boolean] = htmlAttr("required"   , BooleanAsAttrPresenceCodec)
  val rows          : HtmlAttr[Int]     = htmlAttr("rows"       , IntAsStringCodec)
  val cols          : HtmlAttr[Int]     = htmlAttr("cols"       , IntAsStringCodec)
  val maxLength     : HtmlAttr[Int]     = htmlAttr("max-length" , IntAsStringCodec)
  val minLength     : HtmlAttr[Int]     = htmlAttr("min-length" , IntAsStringCodec)
  val pattern       : HtmlAttr[String]  = htmlAttr("pattern"    , StringAsIsCodec)
  val placeholder   : HtmlAttr[String]  = htmlAttr("placeholder", StringAsIsCodec)
  val readOnly      : HtmlAttr[Boolean] = htmlAttr("readonly"   , BooleanAsAttrPresenceCodec)
  val textFieldType : HtmlAttr[String]  = htmlAttr("type"       , StringAsIsCodec) // "text", "email", "number", "password", "search", "tel", "url", "textarea"

  def filled  (modifiers: Modifier[HtmlElement]*) = htmlTag("md-filled-text-field")  (modifiers*)
  def outlined(modifiers: Modifier[HtmlElement]*) = htmlTag("md-outlined-text-field")(modifiers*)
