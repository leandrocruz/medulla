package medulla

object ui {

  import com.raquo.laminar.api.L.*

  class SimpleGridLayout(
    logo: Signal[HtmlElement],
    top : Signal[HtmlElement],
    left: Signal[HtmlElement],
    main: Signal[HtmlElement]) {
    def render: HtmlElement = div( /* See style.css */
      idAttr("medulla"),
      div(idAttr("logo"), child <-- logo),
      div(idAttr("top") , child <-- top),
      div(idAttr("left"), child <-- left),
      div(idAttr("main"), child <-- main)
    )
  }
}