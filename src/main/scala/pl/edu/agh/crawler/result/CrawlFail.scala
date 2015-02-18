package pl.edu.agh.crawler.result

import pl.edu.agh.crawler.task.SingleTask

case class CrawlFail(task: SingleTask, cause: Throwable) extends CrawlResult
