package pl.edu.agh.crawler.config

import com.typesafe.config.ConfigFactory

import scala.util.Try

object crawlerConfig {
  val config = ConfigFactory.load()

  val phantomjsPath = config.getString("crawler.phantomjs.path")
  val pageLoadTimeout = config.getInt("crawler.timeout.pageLoad")
  val ajaxCompleteTimeout = config.getInt("crawler.timeout.ajaxComplete")
  val domStableTimeout = config.getInt("crawler.timeout.domStable")
  val scrollEffectAjaxTimeout = config.getInt("crawler.timeout.scrollEffectAjax")
  val scrollEffectRenderTimeout = config.getInt("crawler.timeout.scrollEffectRender")
  val userAgent: Option[String] = Try(config.getString("crawler.phantomjs.userAgent")).toOption
  val phantomLoadImages = config.getBoolean("crawler.phantomjs.loadImages")

  val excludedResources = config.getStringList("crawler.excludedResources")
}
