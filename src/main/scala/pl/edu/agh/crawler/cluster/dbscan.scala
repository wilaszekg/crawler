package pl.edu.agh.crawler.cluster

import pl.edu.agh.crawler.browser.PageElement


abstract class Cluster {
  def flatten: List[PageElement]
}

case class Leaf(val element: PageElement) extends Cluster {
  override def flatten: List[PageElement] = List(element)
}

case class Group(val left: Cluster, val right: Cluster) extends Cluster {
  override def flatten: List[PageElement] = left.flatten ::: right.flatten
}


object dbscan {

  val epsilon = 0.15

  private def distance(element: PageElement, cluster: Cluster): Double = {
    cluster match {
      case Leaf(otherElement) => element.distanceTo(otherElement)
      case Group(left, right) => math.max(distance(element, left), distance(element, right))
    }
  }

  private def distance(first: Cluster, second: Cluster): Double = {
    first match {
      case Leaf(element) => distance(element, second)
      case Group(left, right) => math.max(distance(left, second), distance(right, second))
    }
  }

  private def selectTwoClosest(clusters: List[Cluster]): (Cluster, Cluster, Double) = {
    clusters match {
      case List(first, second) => (first, second, distance(first, second))
      case head :: tail =>
        val closest: Cluster = tail.minBy(distance(head, _))
        val closestDistance = distance(head, closest)
        selectTwoClosest(tail) match {
          case (first, second, d) if d < closestDistance => (first, second, d)
          case _ => (head, closest, closestDistance)
        }
    }

  }

  private def joinClusters(clusters: List[Cluster]): List[Cluster] = {
    selectTwoClosest(clusters) match {
      case (first, second, d) if d <= epsilon =>
        joinClusters(new Group(first, second) :: clusters.diff(List(first, second)))
      case _ => clusters
    }
  }

  def cluster(elements: List[PageElement]) = {
    joinClusters(elements.map(new Leaf(_))).map(_ flatten)
  }

}
