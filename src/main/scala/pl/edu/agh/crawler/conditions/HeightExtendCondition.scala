package pl.edu.agh.crawler.conditions

import com.google.common.base.Predicate
import org.fluentlenium.core.Fluent
import pl.edu.agh.crawler.browser.Browser

class HeightExtendCondition(val browser: Browser, initialHeight: Long) extends Predicate[Fluent] {
  val minimalChangeToNotice = 100

  override def apply(fluent: Fluent): Boolean =
    browser.documentHeight - initialHeight > minimalChangeToNotice
}
