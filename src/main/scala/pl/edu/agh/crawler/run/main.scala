package pl.edu.agh.crawler.run

import pl.edu.agh.crawler.api.{MultiCrawler, CrawlerPool}
import pl.edu.agh.crawler.description.{CrawlResult, CrawlingTask}

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Promise


object main {

  val results = new mutable.Queue[CrawlResult]

  val monitor = new Object

  val urls = List("https://pl-pl.facebook.com/WislaKrakow",
    "https://www.facebook.com/uefachampionsleague",
    "https://www.facebook.com/ChelseaFC",
    "https://twitter.com/barackobama",
    "https://twitter.com/WislaKrakowSA",
    "https://twitter.com/dalailama",
    "https://twitter.com/pontifex",
    "https://es-es.facebook.com/RealMadrid",
    "https://twitter.com/realmadrid",
    "https://www.facebook.com/VisitFlorence"/*,
    "https://pl-pl.facebook.com/WislaKrakow",
    "https://www.facebook.com/uefachampionsleague",
    "https://www.facebook.com/ChelseaFC",
    "https://twitter.com/barackobama",
    "https://twitter.com/WislaKrakowSA",
    "https://twitter.com/dalailama",
    "https://twitter.com/pontifex",
    "https://es-es.facebook.com/RealMadrid",
    "https://twitter.com/realmadrid",
    "https://www.facebook.com/VisitFlorence"*/
  )

  private def handleResult(result: Promise[CrawlResult]) = results.synchronized {
    result.future onSuccess {
      case r => results += r
        println(r)
        monitor.synchronized {
          if (results.size == urls.size)
            monitor.notifyAll()
        }
    }
    result.future onFailure {
      case e => results += new CrawlResult(null, null, null, null)
        println("Promise failure")
    }
  }

  def main(args: Array[String]) {
    val crawlerPool: CrawlerPool = new CrawlerPool(4)

    val multiCrawler = new MultiCrawler(crawlerPool)

    val start: Long = System.currentTimeMillis()

    urls foreach (url => handleResult(multiCrawler.crawl(new CrawlingTask(url))))

    monitor.synchronized {
      monitor.wait()
    }


    val end: Long = System.currentTimeMillis()

    println("ELAPSED TIME: " + (end - start))

    results.zipWithIndex.foreach { case (result, i) => util.dump(i, result)}
  }
}
