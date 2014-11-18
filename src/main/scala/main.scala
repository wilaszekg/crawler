import java.io.{File, PrintWriter}

import pl.edu.agh.crawler._


object main {

  val urls = List("https://pl-pl.facebook.com/WislaKrakow",
    "https://www.facebook.com/uefachampionsleague",
    "https://www.facebook.com/ChelseaFC",
    "http://scala-lang.org/",
    "https://twitter.com/WislaKrakowSA",
    "http://www.iet.agh.edu.pl/pl/studenci/aktualnosci/",
    "http://www.iet.agh.edu.pl/pl/o-wydziale/wladze/",
    "http://www.kia.com/pl/",
    "http://www.ryanair.com/",
    "http://pogoda.onet.pl/prognoza-pogody/dzis/europa,polska,krakow,9202.html"
  )

  def dump(i: Int, result: CrawlResult) {
    println(result.task.url)
    println("Load time: " + result.statistics.loadTime)
    println("Crawl time: " + result.statistics.crawlTime)
    //println(result.removedContent)

    val writer: PrintWriter = new PrintWriter(new File("crawled" + i + ".html"))
    writer.write(result.pageContent)
    writer.close()
  }

  def main(args: Array[String]) {
    val crawlerPool: CrawlerPool = new CrawlerPool(4)
    val dispatcher: TasksDispatcher = new TasksDispatcher(crawlerPool, urls map (new CrawlingTask(_)))

    val start: Long = System.currentTimeMillis()
    dispatcher.execute

    dispatcher.monitor.synchronized {
      dispatcher.monitor.wait()
    }
    val end: Long = System.currentTimeMillis()

    println("ELAPSED TIME: " + (end - start))

    dispatcher.results.zipWithIndex.foreach { case (result, i) => dump(i, result)}
  }
}
