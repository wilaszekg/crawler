package pl.edu.agh.crawler.run

import java.io.{File, PrintWriter}

import pl.edu.agh.crawler.description.CrawlResult

object util {


  def dump(i: Int, result: CrawlResult, suffix: String = "") {
    println(result.task.url)
    println("Load time: " + result.statistics.loadTime)
    println("Crawl time: " + result.statistics.crawlTime)
    println("Scroll time: " + result.statistics.scrollTime)
    println(result.removedContent)

    val writer: PrintWriter = new PrintWriter(new File("crawled" + i + suffix + ".html"))
    writer.write(result.pageContent)
    writer.close()
  }
}
