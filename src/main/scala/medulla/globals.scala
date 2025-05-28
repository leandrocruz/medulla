package medulla

trait UserToken[UID] {
  def id    : UID
  def name  : String
  def email : String
}

object Globals {

  import com.raquo.laminar.api.L.*

  val user        = new EventBus[Option[UserToken[_]]]
  val currentUser = user.events.distinct.toSignal(None)
}
