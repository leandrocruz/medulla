package medulla.ui.dnd

import org.scalajs.dom.MouseEvent
import com.raquo.laminar.api.L.*

object Point {
  def empty = Point(0, 0)
  def of(evt: MouseEvent) = Point(evt.clientX, evt.clientY)
  def of(el: HtmlElement) = Point(el.ref.offsetLeft, el.ref.offsetTop)
}

case class Point(x: Double, y: Double) {
  def top  = y + "px"
  def left = x + "px"
  def +(other: Point) = Point(x + other.x, y + other.y)
  def -(other: Point) = Point(x - other.x, y - other.y)
  override def toString = s"($x, $y)"
}

object DragAndDrop {

  def apply(el: HtmlElement, debug: Boolean = false): HtmlElement = {

    val diff      = Var(Option.empty[Point])
    val moves     = EventBus[Option[Point]]()
    val positions = moves.events.filter(_.isDefined).map(_.get)

    def initialDiff(point: Point)                       = Some(point - Point.of(el))
    def onMove     (point: Point, maybe: Option[Point]) = maybe.map { point - _ }

    def wrap(result: HtmlElement) = {
      div(
        div(
          position("fixed"),
          top  := "0px",
          left := "0px",
          button("Move", onClick.mapTo(Some(Point(-500, 500))) --> moves),
        ),
        result
      )
    }

    val result = el.amend(
      position("fixed"),
      top   <-- positions.map(_.top),
      left  <-- positions.map(_.left),
      onMouseUp  .mapTo(None) --> diff,
      onMouseDown.map(Point.of).map(initialDiff) --> diff,
      onMouseMove.map(Point.of).compose(_.withCurrentValueOf(diff).map(onMove)) --> moves,
    )

    if(debug) wrap(result) else result
  }
}