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
    println(formatOutput(sp(dijkstraFunc(locations, start, end), start, end)))
  }

  def formatOutput(distPath: Tuple2[Int,Buffer[String]]): Map[String,Any] = {
    // Create map with the requested format
    var (distance, path) = distPath
    val output = new HashMap[String, Any]
    output("distance") = distance
    output("path") = path.mkString(" => ")
    return output
  }
  
  def dijkstraFunc(locations: List[Map[String,Any]], start: String, end: String):
        Tuple2[Map[String,Int],Map[String,String]] = {
    var distances : Map[String, Int] = new HashMap()
    var predecessors : Map[String, String] = new HashMap()
    
    var locs = locations
    // Get the set of unique nodes
    var unvisited: Set[String] = (Set(locations.map({x => x("startLocation").toString()}): _*)
                                ++ Set(locations.map({x => x("endLocation").toString()}): _*))                       
    var current: String = start
    distances(current) = 0
    
    while(unvisited.size > 0){
      // Get the list of edges between current and unvisited neighbors 
      var neighbors = locs.filter(x => ( x("startLocation") == current
                                            && unvisited.contains(x("endLocation").toString()) )
                                        || ( x("endLocation") == current
                                            && unvisited.contains(x("startLocation").toString())) )
      for (neighbor <- neighbors){
        // get the string representation of neighbor in either position
        var n: String = if (neighbor("startLocation") == current)
                            neighbor("endLocation").toString()
                        else neighbor("startLocation").toString()
        // use scala's kludgy type casting replacement to get an Int
        val d: Any = neighbor("distance")
        val distance = (d match{
          case x:Int => x
          case _ => 0
        })
        var new_dist = distance + distances(current)
        // determine whether new distance to neighbor is shorter than previous minimum, if any
        if(!distances.contains(n) || new_dist < distances(n)){
          distances(n) = new_dist
          predecessors(n) = current 
        }
        // remove the edge -- we won't use it again
        locs = locs.filterNot(x => x == neighbor)
      }
      unvisited.remove(current)
      var relevantUnvisited = distances.filterKeys{ unvisited }
      // return if we have no relevant unvisited nodes or at our destination
      // this catches no-path edge cases
      if (relevantUnvisited.size == 0 || current == end){
        return (distances, predecessors)
      }
      // Get the key corresponding to the shortest path
      current = relevantUnvisited.minBy(_._2)._1 
    }
    // should only ever reach here if we don't have a path
    return (distances, predecessors)
  }
    
  def sp(distPath: Tuple2[Map[String,Int],Map[String,String]], start: String, end: String): Tuple2[Int,Buffer[String]] = {
    var (distances, paths) = distPath
    var dist = -1
    try{
      dist = distances(end)
    } catch {
      case nsee: NoSuchElementException =>
    }
    var p : Buffer[String] = Buffer[String]()
    try{
      p.prepend(end)
      var e = end
      while(e != start){
        e = paths(e)
        p.prepend(e)
      }
    } catch {
      case nsee: NoSuchElementException =>
    }
    return (dist, p)
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
}