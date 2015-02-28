package pl.edu.agh.crawler.task

case class ComposedTask(tasks: List[SingleTask], clearCookies: Boolean = true) extends CrawlTask
