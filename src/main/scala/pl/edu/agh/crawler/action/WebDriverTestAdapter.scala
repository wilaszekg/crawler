package pl.edu.agh.crawler.action

trait WebDriverTestAdapter extends SeleniumTestSourceAdapter {

  override def adoptToRecordedAction(source: String, actionClassName: String): String = {
    val classCompilationUnit = new ClassCompilationUnit(source)
    classCompilationUnit.removePackage()
    classCompilationUnit.addClassImport("pl.edu.agh.crawler.action.RecordedAction")
    classCompilationUnit.setExtends("RecordedAction")
    classCompilationUnit.removeField("driver")
    classCompilationUnit.renameMethodAnnotatedAs("runAction", "Test")
    classCompilationUnit.removeAllUsagesOf("driver", "setUp")
    classCompilationUnit.setClassName(actionClassName)

    classCompilationUnit.toString
  }
}
