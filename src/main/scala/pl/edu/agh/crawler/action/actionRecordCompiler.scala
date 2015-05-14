package pl.edu.agh.crawler.action

import net.openhft.compiler.CachedCompiler

object ActionRecordCompiler {
  val nameIterator = Stream.from(1).iterator

  private def generateNextActionName = synchronized {
    "AghCrawlerRecordedAction" + ActionRecordCompiler.nameIterator.next()
  }
}

class ActionRecordCompiler {

  this: SeleniumTestSourceAdapter =>


  def compile(source: String): ActionSupplier = {
    val actionClassName: String = ActionRecordCompiler.generateNextActionName
    val modifiedSource: String = adoptToRecordedAction(source, actionClassName)
    val compiler: CachedCompiler = new CachedCompiler(null, null)
    val actionClass = compiler.loadFromJava(actionClassName, modifiedSource).asInstanceOf[Class[RecordedAction]]

    new ActionSupplier(actionClass)
  }


}

class DriverBackedActionRecordCompiler extends ActionRecordCompiler with DriverBackedTestAdapter
