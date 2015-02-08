package pl.edu.agh.crawler.action

import net.openhft.compiler.CachedCompiler

object actionRecordCompiler {

  def compile(source: String): ActionSupplier = {
    val modifiedSource: String = adoptToRecordedAction(source)
    val compiler: CachedCompiler = new CachedCompiler(null, null)
    val actionClass = compiler.loadFromJava("Behaviour", modifiedSource).asInstanceOf[Class[RecordedAction]]

    new ActionSupplier(actionClass)
  }

  private def adoptToRecordedAction(source: String): String = {
    val actionCompilationUnit = new ClassCompilationUnit(source)
    actionCompilationUnit.addClassImport("pl.edu.agh.crawler.action.RecordedAction")
    actionCompilationUnit.setExtends("RecordedAction")
    actionCompilationUnit.removeField("driver")
    actionCompilationUnit.renameMethodAnnotatedAs("runAction", "Test")
    actionCompilationUnit.removeUsagesOf("driver", "setUp")

    actionCompilationUnit.toString
  }
}
