package medulla

trait UserToken {
  def name  : String
  def email : String
}

object Globals {

  import com.raquo.laminar.api.L.*

  val user        = new EventBus[Option[UserToken]]
  val currentUser = user.events.distinct.toSignal(None)
}
