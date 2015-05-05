package pl.edu.agh.crawler.result

import java.io.File

trait JobResult

case class ScrollResult(successAttempts: Int, time: Long) extends JobResult

case class CrawlResult(time: Long) extends JobResult

case class ScreenShotResult(screen: File, time: Long) extends JobResult

case class CrawlingStatistics(loadTime: Long, elapsedTime: Long, jobs: List[JobResult], ajaxRequestsCount: Number)

case class RemovedNode(content: String, xpath: String)