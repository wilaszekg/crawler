package pl.edu.agh.crawler.task

import pl.edu.agh.crawler.action.ActionSupplier

case class ComposedTask(tasks: List[SingleTask], authAction: ActionSupplier) extends CrawlTask
