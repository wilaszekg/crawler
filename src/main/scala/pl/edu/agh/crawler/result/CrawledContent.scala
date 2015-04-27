package pl.edu.agh.crawler.result

case class CrawledContent(pageContent: String, removedContent: Seq[String], iFramesBySrc: Map[String, String])
