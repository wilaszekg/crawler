package pl.edu.agh.crawler.cluster

import pl.edu.agh.crawler.browser.PageElement

object positionGrouping {
  def buildClusters(elements: List[PageElement]) = {
    elements.groupBy(_.position.left)
  }
}
