package pl.edu.agh.crawler.task

case class SingleTask(url: String,
                      depth: Int,
                      getPageSourceOnly: Boolean = false,
                      scrollAttempts: Int = 0) extends CrawlTask
