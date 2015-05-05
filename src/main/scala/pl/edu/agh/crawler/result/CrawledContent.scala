package pl.edu.agh.crawler.result

case class CrawledContent(pageContent: String, removedContent: Seq[RemovedNode], iFramesBySrc: Map[String, String])
