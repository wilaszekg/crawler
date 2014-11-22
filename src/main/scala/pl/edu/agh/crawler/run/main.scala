package pl.edu.agh.crawler.run

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

    dispatcher.results.zipWithIndex.foreach { case (result, i) => util.dump(i, result)}
  }
}
