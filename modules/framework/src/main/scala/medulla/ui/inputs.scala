package medulla.ui.inputs

import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.*
import com.raquo.laminar.modifiers.RenderableNode
import com.raquo.laminar.nodes.ChildNode.Base
import medulla.utils.Signals
import scala.util.{Failure, Success, Try}

private val NullValue = ""

enum InputType:
  case text, password

trait InputVar[T] {
  def signal                   : Signal[Option[String]]
  def value                    : Signal[Option[Try[T]]]
  def flip                     : Signal[Try[Option[T]]]
  def now                      : Option[String]
  def valueNow                 : Option[Try[T]]
  def read                     : Signal[String]
  def write(str: String)       : Unit
  def set(str: Option[String]) : Unit
  def maybe                    : Signal[Try[Option[T]]]
  def must                     : Signal[Try[T]]
}

private case class InputVarImpl[T](
  text      : Var[Option[String]],
  codec     : SafeConverter[T]
) extends InputVar[T] {
  override def signal                     : Signal[Option[String]] = text.signal
  override def now                        : Option[String]         = text.signal.now()
  override def value                      : Signal[Option[Try[T]]] = text.signal.map(_.map(codec.fromText))
  override def valueNow                   : Option[Try[T]]         = text.now().map(codec.fromText)
  override def read                       : Signal[String]         = text.signal.map(_.getOrElse(NullValue))
  override def write (str: String)        : Unit                   = text.set(Some(str))
  override def set   (str: Option[String]): Unit                   = text.set(str)

  override def flip: Signal[Try[Option[T]]] = signal.map {
    case None                                 => Success(None)
    case Some(text) if text.trim == NullValue => Success(None)
    case Some(text)                           => codec.fromText(text).map(Some(_))
  }

  override def maybe: Signal[Try[Option[T]]] = flip

  override def must: Signal[Try[T]] = flip.map {
    case Success(None)    => Failure(Exception("Missing value"))
    case Success(Some(t)) => Success(t)
    case Failure(err)     => Failure(err)
  }
}

object InputVar {
  import com.raquo.airstream.combine.generated.StaticSignalCombineOps

  def of[T](maybe: Option[T])(using codec: SafeConverter[T]): InputVar[T] = InputVarImpl(Var(maybe.flatMap(t => codec.asText(t).toOption)), codec)
  def of[T](text: String)    (using codec: SafeConverter[T]): InputVar[T] = of(codec.fromText(text).toOption)
  def of[T](t: T)            (using codec: SafeConverter[T]): InputVar[T] = of(Some(t))
  def of[T]                  (using codec: SafeConverter[T]): InputVar[T] = of(None)

  def combine[A, B, Out](dftl: => Out)(sa: Signal[Try[A]], sb: Signal[Try[B]])(combinator: (A, B) => Out): Signal[Out] = {
    StaticSignalCombineOps.combineWithFn(sa, sb) {
      case (Success(va), Success(vb)) => combinator(va, vb)
      case _ => dftl
    }
  }

  def combine[A, B, C, Out](dftl: => Out)(sa: Signal[Try[A]], sb: Signal[Try[B]], sc: Signal[Try[C]])(combinator: (A, B, C) => Out): Signal[Out] = {
    StaticSignalCombineOps.combineWithFn(sa, sb, sc) {
      case (Success(va), Success(vb), Success(vc)) => combinator(va, vb, vc)
      case _ => dftl
    }
  }

  def combine[A, B, C, D, Out](dftl: => Out)(sa: Signal[Try[A]], sb: Signal[Try[B]], sc: Signal[Try[C]], sd: Signal[Try[D]])(combinator: (A, B, C, D) => Out): Signal[Out] = {
    StaticSignalCombineOps.combineWithFn(sa, sb, sc, sd) {
      case (Success(va), Success(vb), Success(vc), Success(vd)) => combinator(va, vb, vc, vd)
      case _ => dftl
    }
  }

  def combine[A, B, C, D, E, Out](dftl: => Out)(sa: Signal[Try[A]], sb: Signal[Try[B]], sc: Signal[Try[C]], sd: Signal[Try[D]], se: Signal[Try[E]])(combinator: (A, B, C, D, E) => Out): Signal[Out] = {
    StaticSignalCombineOps.combineWithFn(sa, sb, sc, sd, se) {
      case (Success(va), Success(vb), Success(vc), Success(vd), Success(ve)) => combinator(va, vb, vc, vd, ve)
      case _ => dftl
    }
  }

  def combine[A, B, C, D, E, F, Out](dftl: => Out)(sa: Signal[Try[A]], sb: Signal[Try[B]], sc: Signal[Try[C]], sd: Signal[Try[D]], se: Signal[Try[E]], sf: Signal[Try[F]])(combinator: (A, B, C, D, E, F) => Out): Signal[Out] = {
    StaticSignalCombineOps.combineWithFn(sa, sb, sc, sd, se, sf) {
      case (Success(va), Success(vb), Success(vc), Success(vd), Success(ve), Success(vf)) => combinator(va, vb, vc, vd, ve, vf)
      case _ => dftl
    }
  }

  def combine[A, B, C, D, E, F, G, Out](dftl: => Out)(sa: Signal[Try[A]], sb: Signal[Try[B]], sc: Signal[Try[C]], sd: Signal[Try[D]], se: Signal[Try[E]], sf: Signal[Try[F]], sg: Signal[Try[G]])(combinator: (A, B, C, D, E, F, G) => Out): Signal[Out] = {
    StaticSignalCombineOps.combineWithFn(sa, sb, sc, sd, se, sf, sg) {
      case (Success(va), Success(vb), Success(vc), Success(vd), Success(ve), Success(vf), Success(vg)) => combinator(va, vb, vc, vd, ve, vf, vg)
      case _ => dftl
    }
  }

  def toTry[A, B](s1: InputVar[A], s2: InputVar[B]): Try[(A, B)] = {
    (s1.valueNow, s2.valueNow) match
      case (Some(Success(a)), Some(Success(b))) => Success(a, b)
      case _                                    => Failure(Exception(""))
  }
}

trait SafeConverter[T] {
  def fromText(str: String): Try[T]
  def asText(t: T)         : Try[String]
}

case class CustomInput[T](in: InputVar[T], node: HtmlElement) {
  def modify(modifiers: Modifier[HtmlElement]*): CustomInput[T] = copy(node = node.amend(modifiers))
}

given SafeConverter[String] = new SafeConverter[String] {
  override def fromText(str: String): Try[String] = Success(str)
  override def asText  (str: String): Try[String] = Success(str)
}

given SafeConverter[Boolean] = new SafeConverter[Boolean] {
  override def fromText(str: String)   : Try[Boolean] = Success(str == "sim" || str == "yes" || str == "true")
  override def asText  (value: Boolean): Try[String]  = if(value) Success("sim") else Success("não")
}

given RenderableNode[CustomInput[_]] = new RenderableNode[CustomInput[_]] {
  override def asNode        (value: CustomInput[_])                         : Base                        = value.node
  override def asNodeOption  (value: Option[CustomInput[_]])                 : Option[Base]                = value.map(_.node)
  override def asNodeSeq     (values: com.raquo.laminar.Seq[CustomInput[_]]) : com.raquo.laminar.Seq[Base] = values.map(_.node)
}

object Select {
  def basic(modifiers: Modifier[HtmlElement]*) = select(cls("medulla"), modifiers)
}

object Input {

  def basic[T](
    in         : InputVar[T],
    kind       : Signal[InputType] = Signal.fromValue(InputType.text),
    isDisabled : Signal[Boolean]   = Signal.fromValue(false),
  ) = CustomInput[T](
    in,
    input(
      cls("medulla"),
      typ <-- kind.map(_.toString),
      disabled <-- isDisabled,
      controlled(
        value <-- in.read,
        onInput.mapToValue --> in.write
      )
    )
  )

  def floating[T](labelText: String)(inner: CustomInput[T]) = CustomInput[T](
    inner.in,
    {
      val isFocused = Var(false)
      val floating  = Signals.and(inner.in.read, isFocused) { _.nonEmpty || _ }
      div(
        cls("medulla floating input"),
        cls.toggle("active") <-- floating,
        label(labelText),
        inner.node.amend(
          onFocus .mapTo(true)  --> isFocused,
          onBlur  .mapTo(false) --> isFocused
        )
      )
    }
  )

  def basicTextArea[T](
    in        : InputVar[T],
    isDisabled: Signal[Boolean] = Signal.fromValue(false)
  ) = CustomInput[T](
    in,
    textArea(
      cls("medulla"),
      disabled <-- isDisabled,
      controlled(
        value <-- in.read,
        onInput.mapToValue --> in.write
      )
    )
  )
}
