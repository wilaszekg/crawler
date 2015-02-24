package pl.edu.agh.crawler.config

import com.typesafe.config.ConfigFactory

object crawlerConfig {
  val config = ConfigFactory.load()

  val phantomjsPath = config.getString("crawler.phantomjs.path")
  val pageLoadTimeout = config.getInt("crawler.timeout.pageLoad")
  val ajaxCompleteTimeout = config.getInt("crawler.timeout.ajaxComplete")
  val domStableTimeout = config.getInt("crawler.timeout.domStable")
  val scrollEffectAjaxTimeout = config.getInt("crawler.timeout.scrollEffectAjax")
  val scrollEffectRenderTimeout = config.getInt("crawler.timeout.scrollEffectRender")

  val excludedResources = config.getStringList("crawler.excludedResources")
}
