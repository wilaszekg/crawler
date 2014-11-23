package pl.edu.agh.crawler.description

class Timer {
  val start = System.currentTimeMillis()

  def measure[T](task: => T) = {
    val result: T = task
    new TimeTask(System.currentTimeMillis() - start, result)
  }
}
