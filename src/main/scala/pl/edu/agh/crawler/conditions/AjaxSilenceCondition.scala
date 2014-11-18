package pl.edu.agh.crawler.conditions

import org.fluentlenium.core.Fluent
import org.openqa.selenium.remote.RemoteWebDriver

class AjaxSilenceCondition extends RepeatableCondition(interval = 50, times = 6) {

  override def expectedCondition(fluent: Fluent) =
    fluent.getDriver.asInstanceOf[RemoteWebDriver].executeScript("return window.ajaxRequests.length == 0").asInstanceOf[Boolean]
}
