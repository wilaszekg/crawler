package pl.edu.agh.crawler.conditions

import com.google.common.base.Predicate
import org.fluentlenium.core.Fluent

abstract class RepeatableCondition(val interval: Int, val times: Int) extends Predicate[Fluent] {

  override def apply(fluent: Fluent): Boolean = keepsTrue(fluent)

  protected def expectedCondition(fluent: Fluent): Boolean

  private def keepsTrue(fluent: Fluent) =
    0 until times forall (x => {
      Thread.sleep(interval)
      expectedCondition(fluent)
    })
}
