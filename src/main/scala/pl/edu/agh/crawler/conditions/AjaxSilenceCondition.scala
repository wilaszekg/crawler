package pl.edu.agh.crawler.conditions

import com.google.common.base.Predicate
import org.fluentlenium.core.Fluent
import org.openqa.selenium.remote.RemoteWebDriver

class AjaxSilenceCondition extends Predicate[Fluent] {
  override def apply(input: Fluent): Boolean = keepsSilent(input, 50, 6)

  private def keepsSilent(fluent: Fluent, interval: Int, times: Int) =
    0 until times forall (x => {
      Thread.sleep(interval)
      noActiveAjaxRequest(fluent)
    })

  private def noActiveAjaxRequest(fluent: Fluent) =
    fluent.getDriver.asInstanceOf[RemoteWebDriver].executeScript("return window.ajaxRequests.length == 0").asInstanceOf[Boolean]
}
