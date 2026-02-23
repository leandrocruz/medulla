package medulla.utils

import com.raquo.laminar.api.L

object Signals {

  import com.raquo.laminar.api.L.{Signal, Var, HtmlElement}
  import scala.scalajs.js

  def and[A, B, C](va: Var[A]   , vb: Var[B])    (fn: (A, B) => C): Signal[C] = va.signal.combineWith(vb.signal).map((a, b) => fn(a, b))
  def and[A, B, C](sa: Signal[A], vb: Var[B])    (fn: (A, B) => C): Signal[C] = sa       .combineWith(vb.signal).map((a, b) => fn(a, b))
  def and[A, B, C](va: Var[A]   , sb: Signal[B]) (fn: (A, B) => C): Signal[C] = va.signal.combineWith(sb)       .map((a, b) => fn(a, b))
  def and[A, B, C](sa: Signal[A], sb: Signal[B]) (fn: (A, B) => C): Signal[C] = sa       .combineWith(sb)       .map((a, b) => fn(a, b))

  def resolve[A, B](s1: Var[Option[A]], s2: Var[Option[B]]): Option[(A, B)] = {
    (s1.now(), s2.now()) match
      case (Some(v1), Some(v2)) => Some(v1, v2)
      case _                    => None
  }

  def resolve[A, B, C](
    s1: Var[Option[A]],
    s2: Var[Option[B]],
    s3: Var[Option[C]]
  ): Option[(A, B, C)] = {
    (s1.now(), s2.now(), s3.now()) match
      case (Some(v1), Some(v2), Some(v3)) => Some(v1, v2, v3)
      case _ => None
  }

  def resolve[A, B, C, D](
    s1: Var[Option[A]],
    s2: Var[Option[B]],
    s3: Var[Option[C]],
    s4: Var[Option[D]]
  ): Option[(A, B, C, D)] = {
    (s1.now(), s2.now(), s3.now(), s4.now()) match
      case (Some(v1), Some(v2), Some(v3), Some(v4)) => Some(v1, v2, v3, v4)
      case _ => None
  }

  def resolve[A, B, C, D, E](
    s1: Var[Option[A]],
    s2: Var[Option[B]],
    s3: Var[Option[C]],
    s4: Var[Option[D]],
    s5: Var[Option[E]],
  ): Option[(A, B, C, D, E)] = {
    (s1.now(), s2.now(), s3.now(), s4.now(), s5.now()) match
      case (Some(v1), Some(v2), Some(v3), Some(v4), Some(v5)) => Some(v1, v2, v3, v4, v5)
      case _ => None
  }

  def resolve[A, B, C, D, E, F](
    s1: Var[Option[A]],
    s2: Var[Option[B]],
    s3: Var[Option[C]],
    s4: Var[Option[D]],
    s5: Var[Option[E]],
    s6: Var[Option[F]],
  ): Option[(A, B, C, D, E, F)] = {
    (s1.now(), s2.now(), s3.now(), s4.now(), s5.now(), s6.now()) match
      case (Some(v1), Some(v2), Some(v3), Some(v4), Some(v5), Some(v6)) => Some(v1, v2, v3, v4, v5, v6)
      case _ => None
  }

  def asSignal(promise: js.Promise[HtmlElement]): Signal[Option[HtmlElement]] = {
    Signal.fromJsPromise(promise)
  }
}

object Vars {
  import com.raquo.laminar.api.L.Var
  extension(v: Var[Boolean])
    def toggle: Unit = v.update( value => !value)
}

object Mediator {

  import com.raquo.laminar.api.L.*
  import scala.util.{Try, Success, Failure}

  trait Mediator[S] {
    def trigger       : Observer[Unit]
    def wire          : Modifier[HtmlElement]
    def disabled      : Signal[Boolean]
    def working       : Signal[Boolean]
    def responses     : EventStream[Try[S]]
    def errors        : EventStream[Option[Throwable]]
    def onlyErrors    : EventStream[Throwable]
    def successes     : EventStream[Option[S]]
    def onlySuccesses : EventStream[S]
  }

  def make[REQ, RES](request: Signal[Try[REQ]], execute: REQ => EventStream[Try[RES]]): Mediator[RES] = {

    val bus     = new EventBus[Unit]
    val results = new EventBus[Try[RES]]
    val busy    = Var(false)
    val tmp     = bus.events.sample(request).flatMapSwitch {
      case Failure(error) => EventStream.fromValue(Failure(error))
      case Success(valid) =>
        busy.set(true) //FIXME: this is ugly
        execute(valid).map { result =>
          busy.set(false)
          result
        }
    }

    new Mediator {
      override def trigger       = bus.writer
      override def responses     = results.events
      override def wire          = tmp --> results
      override def disabled      = Signals.and(busy, request) { _ || _.isFailure }
      override def working       = busy.signal
      override def successes     = responses.map(_.toOption)
      override def onlySuccesses = successes.filter(_.isDefined).map(_.get)
      override def errors        = responses.map(_.toEither.swap.toOption)
      override def onlyErrors    = errors.filter(_.isDefined).map(_.get)
    }
  }
}
