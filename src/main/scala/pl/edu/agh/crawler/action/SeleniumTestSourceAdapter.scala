package pl.edu.agh.crawler.action

trait SeleniumTestSourceAdapter {
  def adoptToRecordedAction(source: String, actionClassName: String): String
}