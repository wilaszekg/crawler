package pl.edu.agh.crawler.result

import pl.edu.agh.crawler.task.SingleTask

case class CrawlingFail(task: SingleTask, cause: Throwable) extends CrawlingResult
