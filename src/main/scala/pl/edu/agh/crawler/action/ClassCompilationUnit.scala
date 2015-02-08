package pl.edu.agh.crawler.action

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import java.util

import japa.parser.JavaParser
import japa.parser.ast.`type`.ClassOrInterfaceType
import japa.parser.ast.body.{BodyDeclaration, ClassOrInterfaceDeclaration, FieldDeclaration, MethodDeclaration}
import japa.parser.ast.expr.{AssignExpr, NameExpr}
import japa.parser.ast.stmt.{ExpressionStmt, Statement}
import japa.parser.ast.{CompilationUnit, ImportDeclaration}

import scala.collection.JavaConversions._

class ClassCompilationUnit(val source: String) {
  val compilationUnit = buildCompilationUnit(source)
  val classDeclaration = getClassDeclaration(compilationUnit)

  def addClassImport(fullClassName: String) = {
    val classNameExpression: NameExpr = new NameExpr(fullClassName)
    compilationUnit.getImports.add(new ImportDeclaration(classNameExpression, false, false))
  }

  def setExtends(classToExtend: String) = {
    classDeclaration.setExtends(List(new ClassOrInterfaceType(classToExtend)))
  }

  def removeField(fieldName: String) = {
    classDeclaration.getMembers.remove(classDeclaration.getMembers.find(
      member => isFieldWithName(member, fieldName)).get)
  }

  def renameMethodAnnotatedAs(newName: String, annotationName: String) = {
    val testMethod: MethodDeclaration = classDeclaration.getMembers
      .find(member => isMethodAnnotatedAs(member, annotationName)).get.asInstanceOf[MethodDeclaration]
    testMethod.setName(newName)
  }

  def removeUsagesOf(fieldName: String, targetMethod: String) = {
    val method: MethodDeclaration = getMethod(targetMethod)
    val statements: util.List[Statement] = method.getBody.getStmts
    val fieldUsages = statements.filter(statement => isFieldAssign(fieldName, statement))
    statements.removeAll(fieldUsages)
  }

  private def isFieldAssign(fieldName: String, statement: Statement): Boolean = {
    try {
      statement.asInstanceOf[ExpressionStmt]
        .getExpression.asInstanceOf[AssignExpr]
        .getTarget.asInstanceOf[NameExpr].getName.equals(fieldName)
    } catch {
      case e: Throwable => false
    }
  }

  override def toString = compilationUnit.toString

  private def buildCompilationUnit(source: String): CompilationUnit = {
    val stream = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8))
    JavaParser.parse(stream)
  }

  private def getClassDeclaration(compilationUnit: CompilationUnit): ClassOrInterfaceDeclaration = {
    compilationUnit.getTypes.get(0).asInstanceOf[ClassOrInterfaceDeclaration]
  }

  private def isFieldWithName(member: BodyDeclaration, fieldName: String): Boolean = {
    member.isInstanceOf[FieldDeclaration] &&
      member.asInstanceOf[FieldDeclaration].getVariables.get(0).getId.getName.equals(fieldName)
  }

  private def isMethodAnnotatedAs(member: BodyDeclaration, annotationName: String): Boolean = {
    member.isInstanceOf[MethodDeclaration] &&
      member.getAnnotations != null &&
      member.getAnnotations.exists(annotation => annotation.getName.getName.equals(annotationName))
  }

  private def getMethod(name: String): MethodDeclaration = {
    classDeclaration.getMembers.find(
      member => member.isInstanceOf[MethodDeclaration]
        && member.asInstanceOf[MethodDeclaration].getName.equals(name))
      .get.asInstanceOf[MethodDeclaration]
  }
}
