package pl.edu.agh.crawler.conditions

import com.google.common.base.Predicate
import org.fluentlenium.core.Fluent
import org.openqa.selenium.remote.RemoteWebDriver

class DomSilenceCondition extends Predicate[Fluent] {

  override def apply(fluent: Fluent): Boolean =
    getAddedNodesCount(fluent) == getAddedNodesCount(fluent, 200)

  private def getAddedNodesCount(fluent: Fluent, delay: Int = 0) = {
    Thread.sleep(delay)
    fluent.getDriver.asInstanceOf[RemoteWebDriver].executeScript("return window.addedNodesCount;").asInstanceOf[Number]
  }
}
