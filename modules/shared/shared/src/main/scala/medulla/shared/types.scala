package medulla.shared.types

trait UserToken[UID] {
  def id    : UID
  def name  : String
  def email : String
}

trait GlobalAppData