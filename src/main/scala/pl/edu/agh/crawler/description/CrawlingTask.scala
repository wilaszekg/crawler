package pl.edu.agh.crawler.description

import pl.edu.agh.crawler.action.ActionSupplier

class CrawlingTask(val url: String,
                   val depth: Int,
                   val scrollAttempts: Int = 0,
                   val authActionSupplier: ActionSupplier = null) {
}
