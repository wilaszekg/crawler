package pl.edu.agh.crawler.browser

case class PageElementResponse(val elements: List[PageElement], var hiddenElements: List[PageElement])
