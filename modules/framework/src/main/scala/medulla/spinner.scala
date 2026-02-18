package medulla

import com.raquo.laminar.api.L.Signal

trait Spinner {
  def busy: Signal[Boolean]
  def start: Unit
  def stop: Unit
  def stopWithError(cause: Throwable): Unit
}

case object NoSpinner extends Spinner {
  override def busy = Signal.fromValue(false)
  override def start = ()
  override def stop = ()
  override def stopWithError(cause: Throwable) = ()
}

