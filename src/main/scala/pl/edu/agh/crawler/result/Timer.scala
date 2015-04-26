package pl.edu.agh.crawler.result

class Timer {
  val start = System.currentTimeMillis()

  def measure[T](task: => T): TimeTask[T] = {
    val result: T = task
    TimeTask(System.currentTimeMillis() - start, result)
  }
}
