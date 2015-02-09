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
    val classCompilationUnit = new ClassCompilationUnit(source)
    classCompilationUnit.addClassImport("pl.edu.agh.crawler.action.RecordedAction")
    classCompilationUnit.setExtends("RecordedAction")
    classCompilationUnit.removeField("driver")
    classCompilationUnit.renameMethodAnnotatedAs("runAction", "Test")
    classCompilationUnit.removeUsagesOf("driver", "setUp")

    classCompilationUnit.toString
  }
}
