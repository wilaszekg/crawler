package pl.edu.agh.crawler.action

import net.openhft.compiler.CachedCompiler
import org.openqa.selenium.WebDriver

object actionRecordCompiler {

  def compile(source: String): WebDriver => RecordedAction = {
    val modifiedSource: String = adoptToRecordedAction(source)
    val compiler: CachedCompiler = new CachedCompiler(null, null)
    val actionClass = compiler.loadFromJava("Behaviour", modifiedSource).asInstanceOf[Class[RecordedAction]]

    driver => actionClass.newInstance().withDriver(driver)
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
