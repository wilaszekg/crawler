package pl.edu.agh.crawler.description

class CrawlResult(val task: CrawlingTask,
                  val pageContent: String,
                  val removedContent: Seq[String],
                  val statistics: CrawlingStatistics) {

}
