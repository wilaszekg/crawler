package pl.edu.agh.crawler.result

import pl.edu.agh.crawler.task.SingleTask

case class SingleResult(task: SingleTask,
                        crawledContent: CrawledContent,
                        statistics: CrawlingStatistics) extends CrawlResult
