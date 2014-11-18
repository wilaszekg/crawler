package pl.edu.agh.crawler

class CrawlResult(val task: CrawlingTask,
                  val pageContent: String,
                  val removedContent: Seq[String],
                  val statistics: CrawlingStatistics) {

}
