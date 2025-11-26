package medulla.ui.layout

import com.raquo.laminar.api.L.*

class SimpleGridLayout(
  logo: Signal[HtmlElement],
  top : Signal[HtmlElement],
  left: Signal[HtmlElement],
  main: Signal[HtmlElement]) {
  def render: HtmlElement = div(
    cls("medulla simple grid-layout"),
    div(cls("logo"), child <-- logo),
    div(cls("top") , child <-- top) ,
    div(cls("left"), child <-- left),
    div(cls("main"), child <-- main)
  )
}
