package pl.edu.agh.crawler.browser

import org.apache.commons.lang3.StringUtils

case class PageElement(val cssPath: String, val cssIdPath: String, val position: PageElementPosition) {
  def distanceTo(other: PageElement): Double = {
    val maxLength = math.max(cssPath.length, other.cssPath.length)
    (StringUtils.getLevenshteinDistance(cssPath, other.cssPath) * 1.0) / maxLength
  }
}



