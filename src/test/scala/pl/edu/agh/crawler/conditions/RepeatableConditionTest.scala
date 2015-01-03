package pl.edu.agh.crawler.conditions

import org.fluentlenium.core.Fluent
import org.mockito.Mockito._
import org.scalatest.{FlatSpec, Matchers}

class RepeatableConditionTest extends FlatSpec with Matchers {

  abstract class Condition {
    def apply: Boolean
  }

  class TestableRepeatableCondition(times: Int) extends RepeatableCondition(0, times) {
    val expectedCondition = mock(classOf[Condition])

    override protected def expectedCondition(fluent: Fluent): Boolean = expectedCondition.apply
  }

  it should "pass in 1 repeat" in {
    val repeatableCondition = new TestableRepeatableCondition(1)
    when(repeatableCondition.expectedCondition.apply).thenReturn(true)

    val result: Boolean = repeatableCondition.apply(null)

    result shouldBe true
    verify(repeatableCondition.expectedCondition, times(1)).apply
  }

  it should "fail for 0 successes per 1 repeat" in {
    val repeatableCondition = new TestableRepeatableCondition(1)
    when(repeatableCondition.expectedCondition.apply).thenReturn(false)

    val result: Boolean = repeatableCondition.apply(null)

    result shouldBe false
    verify(repeatableCondition.expectedCondition, times(1)).apply
  }

  it should "pass in 2 repeats" in {
    val repeatableCondition = new TestableRepeatableCondition(2)
    when(repeatableCondition.expectedCondition.apply).thenReturn(true)

    val result: Boolean = repeatableCondition.apply(null)

    result shouldBe true
    verify(repeatableCondition.expectedCondition, times(2)).apply
  }

  it should "fail and stop checking when first attempt fails in 2 repeats" in {
    val repeatableCondition = new TestableRepeatableCondition(2)
    doReturn(false).doReturn(true).when(repeatableCondition.expectedCondition).apply

    val result: Boolean = repeatableCondition.apply(null)

    result shouldBe false
    verify(repeatableCondition.expectedCondition, times(1)).apply
  }

  it should "fail when second attempt fails in 2 repeats" in {
    val repeatableCondition = new TestableRepeatableCondition(2)
    doReturn(true).doReturn(false).when(repeatableCondition.expectedCondition).apply

    val result: Boolean = repeatableCondition.apply(null)

    result shouldBe false
    verify(repeatableCondition.expectedCondition, times(2)).apply
  }
}
