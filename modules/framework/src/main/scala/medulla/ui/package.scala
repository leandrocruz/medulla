package medulla

package object ui {
  val Buttons     = medulla.ui.buttons.Buttons
  val Input       = medulla.ui.inputs.Input
  val Select      = medulla.ui.inputs.Select
  val Modal       = medulla.ui.modal.Modal
  val Overlay     = medulla.ui.overlay.Overlay
  val Login       = medulla.ui.login.Login
  val DragAndDrop = medulla.ui.dnd.DragAndDrop
  val Point       = medulla.ui.dnd.Point

  type SimpleGridLayout = medulla.ui.layout.SimpleGridLayout
  type Point            = medulla.ui.dnd.Point

  export medulla.ui.extensions.Extensions.*
}
