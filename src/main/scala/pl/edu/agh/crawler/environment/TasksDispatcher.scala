package pl.edu.agh.crawler.environment

import pl.edu.agh.crawler.description.{CrawlResult, CrawlingTask}
import pl.edu.agh.crawler.Crawler

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, future}

class TasksDispatcher(val crawlerPool: CrawlerPool, val tasks: Seq[CrawlingTask]) {
  private val queue: mutable.Queue[CrawlingTask] = new mutable.Queue()
  tasks.foreach(queue.enqueue(_))

  var finishedTasks = 0

  val results = new mutable.Queue[CrawlResult]()
  val monitor = new Object

  def execute: Unit = {
    crawlerPool.getFreeCrawler match {
      case Some(crawler) =>
        nextTask(crawler)
        execute
      case None =>
    }
  }

  private def nextTask(crawler: Crawler) = nextFutureTask(crawler) match {
    case Some(futureTask) => {
      futureTask onComplete {
        case _ => {
          crawlerPool.releaseCrawler(crawler)
          this.synchronized {
            finishedTasks += 1
            if (finishedTasks == tasks.length) {
              monitor.synchronized {
                monitor.notifyAll()
              }
            }
          }
          execute
        }
      }
      futureTask onSuccess {
        case result => results += result
      }
      futureTask onFailure {
        case e => e.printStackTrace()
      }
    }
    case None =>
  }

  private def nextFutureTask(crawler: Crawler): Option[Future[CrawlResult]] = this.synchronized {
    if (!queue.isEmpty) {
      val task: CrawlingTask = queue.dequeue()
      Some(future {
        crawler.crawl(task)
      })
    } else None
  }

}
