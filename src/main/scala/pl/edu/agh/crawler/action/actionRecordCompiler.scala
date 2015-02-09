package pl.edu.agh.crawler.action

import net.openhft.compiler.CachedCompiler

object actionRecordCompiler {

  val nameIterator = Stream.from(1).iterator

  def compile(source: String): ActionSupplier = {
    val actionClassName: String = generateNextActionName
    val modifiedSource: String = adoptToRecordedAction(source, actionClassName)
    val compiler: CachedCompiler = new CachedCompiler(null, null)
    val actionClass = compiler.loadFromJava(actionClassName, modifiedSource).asInstanceOf[Class[RecordedAction]]

    new ActionSupplier(actionClass)
  }

  private def generateNextActionName = {
    "AghCrawlerRecordedAction" + nameIterator.next()
  }

  private def adoptToRecordedAction(source: String, actionClassName: String): String = {
    val classCompilationUnit = new ClassCompilationUnit(source)
    classCompilationUnit.addClassImport("pl.edu.agh.crawler.action.RecordedAction")
    classCompilationUnit.setExtends("RecordedAction")
    classCompilationUnit.removeField("driver")
    classCompilationUnit.renameMethodAnnotatedAs("runAction", "Test")
    classCompilationUnit.removeUsagesOf("driver", "setUp")
    classCompilationUnit.setClassName(actionClassName)

    classCompilationUnit.toString
  }
}
