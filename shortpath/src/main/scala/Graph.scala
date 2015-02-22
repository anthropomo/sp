package main.scala

import scala.collection.immutable.Set
import scala.collection.mutable.HashMap
import scala.collection.mutable.Map
import scala.collection.mutable.Buffer

class Graph {
  var nodes : Set[String] = Set()
  var edges : Map[String, Buffer[String]] = new HashMap()
  var distances : Map[Tuple2[String,String],Int] = new HashMap()
  
  
  def add_node(node: String) = {
    nodes = nodes ++ Set(node)
  }
  
  def add_edge(from_node: String, to_node: String, distance: Int) = {
    if(!edges.contains(from_node)){
      edges(from_node) = Buffer[String]()
    }
    edges(from_node).append(to_node)
    if(!edges.contains(to_node)){
      edges(to_node) = Buffer[String]()
    }
    edges(to_node).append(from_node)
    distances((from_node, to_node)) = distance
    distances((to_node, from_node)) = distance
  }
  
}

object Graph {
  def graphFromList(list: List[Map[String,Any]]): Graph = {
    var graph: Graph = new Graph()
    for(map <- list){
      val s: Any = map("startLocation")
      val start = (s match{
        case x:String => x
        case _ => ""
      })
      val e: Any = map("endLocation")
      val end = (e match{
        case x:String => x
        case _ => ""
      })
      val d: Any = map("distance")
      val distance = (d match{
        case x:Int => x
        case _ => Int.MinValue
      })
      graph.add_node(start)
      graph.add_node(end)
      graph.add_edge(start, end, distance)
    }
    return graph
  }
}