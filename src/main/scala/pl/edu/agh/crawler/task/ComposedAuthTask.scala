package pl.edu.agh.crawler.task

import pl.edu.agh.crawler.action.ActionSupplier

case class ComposedAuthTask(composedTask: ComposedTask, authAction: ActionSupplier) extends CrawlTask
