organization := "pl.edu.agh"

name := "crawler"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.seleniumhq.selenium" % "selenium-java" % "2.43.1",
  "com.github.detro.ghostdriver" % "phantomjsdriver" % "1.1.0",
  "org.fluentlenium" % "fluentlenium-core" % "0.10.2",
  "com.google.code.findbugs" % "jsr305" % "1.3.+",
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "com.typesafe" % "config" % "1.2.1",
  "com.google.code.javaparser" % "javaparser" % "1.0.11",
  "net.openhft" % "compiler" % "2.2.0",
  "junit" % "junit" % "4.12",
  "org.scalatest" % "scalatest_2.10" % "2.2.2" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test",
  "org.mockito" % "mockito-all" % "1.10.8" % "test"
)