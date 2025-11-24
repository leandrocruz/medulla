package medulla

object Globals {

  import medulla.shared.types.{GlobalAppData, UserToken}

  import com.raquo.laminar.api.L.*

  val userUpdates = new EventBus[Option[UserToken[_]]]
  val user        = userUpdates.events.distinct.toSignal(None)

  val appDataUpdates = new EventBus[Option[GlobalAppData]]
  val appData        = appDataUpdates.events.distinct.toSignal(None)
}
