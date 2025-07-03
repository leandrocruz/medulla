package medulla.ui.layout

import com.raquo.laminar.api.L.*

class SimpleGridLayout(
  logo: Signal[HtmlElement],
  top : Signal[HtmlElement],
  left: Signal[HtmlElement],
  main: Signal[HtmlElement]) {
  def render: HtmlElement = div(idAttr("medulla"), cls("text-black font-sans min-h-screen grid md:grid-cols grid-rows"),
    div(idAttr("logo") , cls("grid")                                                                                        , child <-- logo),
    div(idAttr("top")  , cls("sticky top-0 flex py-3.5 px-6 gap-x-4 justify-end items-center h-full text-white bg-gray-600"), child <-- top) ,
    div(idAttr("left") , cls("p")                                                                                           , child <-- left),
    div(idAttr("main") , cls("h-full w-full relative p bg-gray-200")                                                        , child <-- main)
  )
}
