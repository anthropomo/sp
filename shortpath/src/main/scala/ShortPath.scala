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

    val graph = Graph.graphFromList(locations)
    var start = "Kruthika's abode"
    var end = "Craig's haunt"
    var (distance, path) = shortestPath(graph, start, end)
 
    val output = new HashMap[String, Any]
    output("distance") = distance
    output("path") = path.mkString(" => ")
    println(output)
  }

  /*
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

    var loop = new Breaks()
    loop.breakable{
      while (unvisited.size > 0){
        // For the current node, consider all of its unvisited neighbors... 
        println(current)
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
          loop.break()
        }
      }
    }
    return (distances, predecessors)
  }
  
  def shortestPath(graph: Graph, start: String, end: String): Tuple2[Int,Buffer[String]] = {
    var (distances, paths) = dijkstra(graph, start, end)
    // get the distance to the end point
    var dist = -1
    println(distances)
    try{
      dist = distances(end)
    } catch {
      case nsee: NoSuchElementException =>
    }
    var p : Buffer[String] = Buffer[String]()
    try{
      p.prepend(end)
      var e = end
      // build the list of nodes on our path from end to beginning
      // probably need a test to see what happens here when there is no path
      while(e != start){
        e = paths(e)
        p.prepend(e)
      }
    } catch {
      case nsee: NoSuchElementException =>
    }
    return (dist, p)
  }
}