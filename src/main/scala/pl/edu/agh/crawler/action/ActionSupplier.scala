package pl.edu.agh.crawler.action

import org.openqa.selenium.WebDriver

class ActionSupplier(actionClass: Class[RecordedAction]) extends (WebDriver => RecordedAction) {

  override def apply(driver: WebDriver): RecordedAction =
    actionClass.newInstance().withDriver(driver)
}
