/*
 * Running this as a Scala Application will compare the two implementations of the
 * shortest path algorithm in ShortPath.scala and print the results to the console.
 */
package test.scala

import main.scala._
import scala.collection.mutable.Map

object PathSpeed{

  def main(args: Array[String]){
    speedTest()
  }
  
  def speedTest(){
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
    val (start, end) = ("Kruthika's abode", "Craig's haunt")
    
    println("Running Functional Version 100000 Times...")
    val t0 = System.currentTimeMillis()
    for(i <- (0 to 100000)){
      ShortPath.shortestPath(ShortPath.dijkstraFunc(locations, start, end), start, end)
    }
    val t1 = System.currentTimeMillis()
    println("Elapsed time: " + (t1 - t0)/1000f + " seconds")
    
    println("Running Imperative/OO Version 100000 Times...")
    val t2 = System.currentTimeMillis()
    for(i <- (0 to 100000)){
      ShortPath.shortestPath(ShortPath.dijkstra(ShortPath.listToGraph(locations), start, end), start, end)
    }
    val t3 = System.currentTimeMillis()
    println("Elapsed time: " + (t3 - t2)/1000f + " seconds")
  }
  
}