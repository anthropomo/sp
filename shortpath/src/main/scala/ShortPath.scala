/*
 * This is the main entry point.
 * 
 * Two versions of the algorithm are presented below.
 * Version 1 was my first attempt and is preserved here to demonstrate my thought
 * process and refactoring. Normally, you would only see it in a git diff.
 * Version 2 is the "final" version. It takes advantage of Functional aspects 
 * of Scala I tried out in the course of the challenge.
 *
 * Running this as will print 
 * Map(distance -> 31, path -> Kruthika's abode => Brian's apartment => Wesley's condo => Bryce's den => Craig's haunt)
 * to the console
 * 
 * To test other inputs use
 * wrapper(locations: List[Map[String,Any]], start: String, end: String): Map[String,Any]
 * 
 * Note, in scenarios with no path, the following output is expected:
 * Map(distance -> -1, path -> )
 */
 
package main.scala
import scala.collection.mutable.Map
import scala.collection.mutable.PriorityQueue
import scala.collection.mutable.Set
import scala.collection.mutable.Buffer
import scala.collection.mutable.HashMap
import scala.util.control.Breaks

object ShortPath {
  def main(args: Array[String]){
    val locations = List(
      Map("startLocation" -> "Kruthika's abode", "endLocation" -> "Mark's crib", "distance" -> 9),
      Map("startLocation" -> "Kruthika's abode", "endLocation" -> "Greg's casa", "distance" -> 4),
      Map("startLocation" -> "Kruthika's abode", "endLocation" -> "Matt's pad", "distance" -> 18),
      Map("startLocation" -> "Kruthika's abode", "endLocation" -> "Brian's apartment", "distance" -> 8),
      Map("startLocation" -> "Brian's apartment", "endLocation" -> "Wesley's condo", "distance" -> 7),
      Map("startLocation" -> "Brian's apartment", "endLocation" -> "Cam's dwelling", "distance" -> 17),
      Map("startLocation" -> "Greg's casa", "endLocation" -> "Cam's dwelling", "distance" -> 13),
      Map("startLocation" -> "Greg's casa", "endLocation" -> "Mike's digs", "distance" -> 19),
      Map("startLocation" -> "Greg's casa", "endLocation" -> "Matt's pad", "distance" -> 14),
      Map("startLocation" -> "Wesley's condo", "endLocation" -> "Kirk's farm", "distance" -> 10),
      Map("startLocation" -> "Wesley's condo", "endLocation" -> "Nathan's flat", "distance" -> 11),
      Map("startLocation" -> "Wesley's condo", "endLocation" -> "Bryce's den", "distance" -> 6),
      Map("startLocation" -> "Matt's pad", "endLocation" -> "Mark's crib", "distance" -> 19),
      Map("startLocation" -> "Matt's pad", "endLocation" -> "Nathan's flat", "distance" -> 15),
      Map("startLocation" -> "Matt's pad", "endLocation" -> "Craig's haunt", "distance" -> 14),
      Map("startLocation" -> "Mark's crib", "endLocation" -> "Kirk's farm", "distance" -> 9),
      Map("startLocation" -> "Mark's crib", "endLocation" -> "Nathan's flat", "distance" -> 12),
      Map("startLocation" -> "Bryce's den", "endLocation" -> "Craig's haunt", "distance" -> 10),
      Map("startLocation" -> "Bryce's den", "endLocation" -> "Mike's digs", "distance" -> 9),
      Map("startLocation" -> "Mike's digs", "endLocation" -> "Cam's dwelling", "distance" -> 20),
      Map("startLocation" -> "Mike's digs", "endLocation" -> "Nathan's flat", "distance" -> 12),
      Map("startLocation" -> "Cam's dwelling", "endLocation" -> "Craig's haunt", "distance" -> 18),
      Map("startLocation" -> "Nathan's flat", "endLocation" -> "Kirk's farm", "distance" -> 3)
    )

    var (start, end) = ("Kruthika's abode", "Craig's haunt")
    println(formatOutput(shortestPath(dijkstraFunc(locations, start, end), start, end)))
  }
  
  /*
   * Accepts the input in the specified format and outputs likewise
   */
  def wrapper(locations: List[Map[String,Any]], start: String, end: String): Map[String,Any] = {
    return formatOutput(shortestPath(dijkstraFunc(locations, start, end), start, end))
  }
   
  def listToGraph(list: List[Map[String,Any]]): Graph = {
    return Graph.graphFromList(list)
  }
  
  def formatOutput(distPath: Tuple2[Int,Buffer[String]]): Map[String,Any] = {
    // Create map with the requested format
    var (distance, path) = distPath
    val output = new HashMap[String, Any]
    output("distance") = distance
    output("path") = path.mkString(" => ")
    return output
  }
  
  /*
   * Version 2
   * Implementation of the algorithm using Functional Programming constructs
   * This version is marginally faster at this scale (see PathSpeed in test)
   */
  def dijkstraFunc(locations: List[Map[String,Any]], start: String, end: String):
        Tuple2[Map[String,Int],Map[String,String]] = {
    var distances : Map[String, Int] = new HashMap()
    var predecessors : Map[String, String] = new HashMap()
    
    var locs = locations
    // Get the set of unique nodes
    var unvisited: Set[String] = (Set(locations.map({x => x("startLocation").toString()}): _*)
                                ++ Set(locations.map({x => x("endLocation").toString()}): _*))
    // Map our data structure into something more iterable
    var neighborLists: Map[String, Buffer[Tuple2[String, Int]]] = 
          Map() ++ (for(x <- unvisited) yield (x, Buffer[Tuple2[String, Int]]()))
    for (x <- locations){
      val dist: Any = x("distance")
      val distance = (dist match{
          case x:Int => x
          case _ => 0
      })
      neighborLists(x("startLocation").toString()).append((x("endLocation").toString(), distance))
      neighborLists(x("endLocation").toString()).append((x("startLocation").toString(), distance))
    }
    var current: String = start
    distances(current) = 0
    
    while(unvisited.size > 0){
    // Get the list of neighbors of current that have not been visited
      var neighbors = neighborLists(current).filter({case (k, _) => unvisited.contains(k)})
      for (neighbor <- neighbors){
        var (name, distance) = neighbor
        var new_dist = distance + distances(current)
    // Determine whether new distance to neighbor is shorter than previous minimum, if any
        if(!distances.contains(name) || new_dist < distances(name)){
          distances(name) = new_dist
          predecessors(name) = current 
        }
      }
      unvisited.remove(current)
      var relevantUnvisited = distances.filterKeys{ unvisited }
    // Return if we have no relevant unvisited nodes
    // this catches no-path edge cases
      if (relevantUnvisited.size == 0){
        return (distances, predecessors)
      }
    // Get the key corresponding to the current shortest path
      current = relevantUnvisited.minBy(_._2)._1
    // Return if we're at our destination
      if (current == end){
        return (distances, predecessors)
      }
    }
    // should only ever reach here if we don't have a path
    return (distances, predecessors)
  }
    
  def shortestPath(distPath: Tuple2[Map[String,Int],Map[String,String]], start: String, end: String): Tuple2[Int,Buffer[String]] = {
    var (distances, paths) = distPath
    var dist = -1
    // Catch edge cases with no path and return -1
    try{
      dist = distances(end)
    } catch {
      case nsee: NoSuchElementException =>
    }
    var p : Buffer[String] = Buffer[String]()
    // Build the path from the end node back
    try{
      p.prepend(end)
      var e = end
      while(e != start){
        e = paths(e)
        p.prepend(e)
      }
    } catch {
    // Avoid returning a partial path if there is no path
      case nsee: NoSuchElementException => p = Buffer[String]()
    }
    return (dist, p)
  }
 
  /*
   * Version 1
   * Imperative/OO version. Based heavily on Wikipedia pseudocode and text.
   * All comments in this function from here:
   * http://en.wikipedia.org/wiki/Dijkstra%27s_algorithm#Algorithm
   */
  def dijkstra(graph: Graph, start: String, end: String): 
        Tuple2[Map[String,Int],Map[String,String]] = {
    var distances : Map[String, Int] = new HashMap()
    var predecessors : Map[String, String] = new HashMap()
    
    // Set the initial node as current. Mark all other nodes unvisited. Create a set of all 
    // the unvisited nodes called the unvisited set.
    var current: String = start
    var unvisited = Set[String]() ++ graph.nodes
    
    // Assign to every node a tentative distance value: set it to zero for our initial node
    // and to infinity for all other nodes.
    distances(current) = 0
    while (unvisited.size > 0){
    // For the current node, consider all of its unvisited neighbors... 
      for (neighbor <- graph.edges(current)){
    // ...calculate their tentative distances.
        var new_dist = graph.distances((current, neighbor)) + distances(current)
    // Compare the newly calculated tentative distance to the current 
    // assigned value and assign the smaller one.
        if (!distances.contains(neighbor) || new_dist < distances(neighbor)){
          distances(neighbor) = new_dist
          predecessors(neighbor) = current 
        }
      } 
    // When we are done considering all of the neighbors of the current node, mark the current 
    // node as visited and remove it from the unvisited set. A visited node will never be 
    // checked again.
      unvisited.remove(current)
    // If the destination node has been marked visited (when planning a route between two 
    // specific nodes) then stop
      current = null
    // Select the unvisited node that is marked with the smallest tentative distance, and set
    // it as the new "current node"
      for (node <- unvisited){
        if (distances.contains(node)){
          if (current == null){
            current = node
          }
          else if(distances(node) < distances(current)){
            current = node
          }
        }
      }
    // if the smallest tentative distance among the nodes in the unvisited 
    // set is infinity (when planning a complete traversal; occurs when there is no connection 
    // between the initial node and remaining unvisited nodes), then stop.
      if (current == null || current == end){
        return (distances, predecessors)
      }
    }
    return (distances, predecessors)
  }
}