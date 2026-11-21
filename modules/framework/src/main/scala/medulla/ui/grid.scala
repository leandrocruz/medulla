package medulla.ui.grid

import com.raquo.laminar.api.L.*
import org.scalajs.dom
import org.scalajs.dom.DragEvent

trait GridItem[ID, STATE] {
  def id    : ID
  def state : STATE
}

case class Lane[STATE](state: STATE, label: String)

abstract class Grid[ID, STATE, ITEM <: GridItem[ID, STATE]](lanes: Seq[Lane[STATE]], items: Signal[Seq[ITEM]]) {

  private val dragging = Var(Option.empty[ID])

  def renderCard(item: ITEM): HtmlElement

  def ammendBody(lane: Lane[STATE], body: HtmlElement): HtmlElement = body

  def renderLane(lane: Lane[STATE]) = {

    def renderItem(id: ID, ignored: ITEM, item: Signal[ITEM]) = {

      def whenDragStarted(id: ID)(evt: DragEvent) = {
        evt.dataTransfer.effectAllowed = dom.DataTransferEffectAllowedKind.move
        dragging.set(Some(id))
      }

      div(
        draggable(true),
        onDragStart           --> whenDragStarted(id),
        onDragEnd.mapTo(None) --> dragging,
        cls("opacity-30")     <-- dragging.signal.map(_.contains(id)),
        child <-- item.map(renderCard)
      )
    }

    val laneItems = items.map(_.filter(_.state == lane.state))

    val header = {
      div(
        cls("flex items-center gap-2 mb-3"),
        h3(cls("text-base font-semibold text-ink"), lane.label),
        span(
          cls("text-xs font-medium text-muted bg-bg rounded-pill px-2 py-0.5 tabular-nums"),
          text <-- laneItems.map(_.length)
        ),
      )
    }

    val body = {
      div(
        cls("flex flex-col gap-3 flex-1 min-h-0 overflow-y-auto"),
//          onDragOver.compose(enrich(lane)) --> whenDragOver,
//          onDrop    .compose(enrich(lane)) --> triggers,
        children <-- laneItems.split(_.id) { renderItem },
      )
    }

    sectionTag(
      cls("flex flex-col min-w-0 min-h-0 h-full"),
      commentNode(s"Lane '${lane.state}' START"),
      header,
      ammendBody(lane, body),
      commentNode(s"Lane '${lane.state}' END"),
    )
  }

  def render = {
    div(
      cls(s"grid grid-cols-${lanes.length} gap-5 min-h-full"),
      lanes.map(renderLane),
    )
  }
}

