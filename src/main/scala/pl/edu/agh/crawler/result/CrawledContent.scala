package pl.edu.agh.crawler.result

case class CrawledContent(pageContent: String,
  removedContent: Seq[RemovedNode],
  removedLinks: Set[String],
  iFramesBySrc: Map[String, String])
