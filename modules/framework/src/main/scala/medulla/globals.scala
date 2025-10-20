package medulla

trait UserToken[UID] {
  def id    : UID
  def name  : String
  def email : String
}

object Globals {

  import com.raquo.laminar.api.L.*

  val userUpdates = new EventBus[Option[UserToken[_]]]
  val user        = userUpdates.events.distinct.toSignal(None)
}
