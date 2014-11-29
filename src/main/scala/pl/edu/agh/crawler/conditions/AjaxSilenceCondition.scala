package pl.edu.agh.crawler.conditions

import org.fluentlenium.core.Fluent
import org.openqa.selenium.phantomjs.PhantomJSDriver

class AjaxSilenceCondition extends RepeatableCondition(interval = 50, times = 6) {

  override def expectedCondition(fluent: Fluent) =
    fluent.getDriver.asInstanceOf[PhantomJSDriver].executePhantomJS("return this.ajaxRequests.length == 0").asInstanceOf[Boolean]
}
