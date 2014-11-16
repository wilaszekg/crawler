name := "crawler"

version := "1.0"

libraryDependencies ++= Seq(
  "org.seleniumhq.selenium" % "selenium-java" % "2.43.1",
  "com.github.detro.ghostdriver" % "phantomjsdriver" % "1.1.0",
  "org.fluentlenium" % "fluentlenium-core" % "0.10.2",
  "com.google.code.findbugs" % "jsr305" % "1.3.+",
  "io.spray" %%  "spray-json" % "1.3.1",
  "org.apache.commons" % "commons-lang3" % "3.3.2"
)