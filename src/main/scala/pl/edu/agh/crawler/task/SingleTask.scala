package pl.edu.agh.crawler.task

trait Job

case class Scroll(attempts: Int) extends Job

case class Crawl(depth: Int) extends Job

class ScreenShot extends Job

case class SingleTask(url: String,
                      jobs: List[Job],
                      getPageSourceOnly: Boolean = false) extends CrawlTask
