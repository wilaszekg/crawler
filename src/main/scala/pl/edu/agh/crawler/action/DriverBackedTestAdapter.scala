package pl.edu.agh.crawler.action

trait DriverBackedTestAdapter extends SeleniumTestSourceAdapter {
  override def adoptToRecordedAction(source: String, actionClassName: String): String = {
    val classCompilationUnit = new ClassCompilationUnit(source)
    classCompilationUnit.removePackage()
    classCompilationUnit.addClassImport("pl.edu.agh.crawler.action.RecordedAction")
    classCompilationUnit.setExtends("RecordedAction")
    classCompilationUnit.renameMethodAnnotatedAs("runAction", "Test")
    classCompilationUnit.removeDeclarationOf("driver", "setUp")
    classCompilationUnit.setClassName(actionClassName)

    classCompilationUnit.toString
  }
}