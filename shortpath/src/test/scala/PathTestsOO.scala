/*
 * Tests using the "Imperative/OO" algorithm implementation.
 */
package test.scala
import org.junit.Assert
import junit.framework.TestCase
import main.scala._
import scala.collection.mutable.Map
import scala.collection.mutable.Buffer

class PathTestsOO extends TestCase{
  
  /*
   * We produce the specified output
   */
  def testAssignment() {
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
    Assert.assertEquals(
        Map("distance" -> 31, "path" -> "Kruthika's abode => Brian's apartment => Wesley's condo => Bryce's den => Craig's haunt"),
        ShortPath.formatOutput(ShortPath.shortestPath(ShortPath.dijkstra(ShortPath.listToGraph(locations), start, end), start, end))
    )
  }
  
  /*
   * We fail in a defined way when there is no path
   */
  def testNoPath() {
    val locations = List(
      Map("startLocation" -> "Kruthika's abode", "endLocation" -> "Mark's crib", "distance" -> 9),
      Map("startLocation" -> "Mike's digs", "endLocation" -> "Nathan's flat", "distance" -> 12),
      Map("startLocation" -> "Cam's dwelling", "endLocation" -> "Craig's haunt", "distance" -> 18),
      Map("startLocation" -> "Nathan's flat", "endLocation" -> "Kirk's farm", "distance" -> 3)
    )
    
    var (start, end) = ("Kruthika's abode", "Craig's haunt")
    var (d, p) = ShortPath.shortestPath(ShortPath.dijkstra(ShortPath.listToGraph(locations), start, end), start, end)
    Assert.assertTrue(d == -1)
    Assert.assertTrue(p.size == 0)
  }
  
  /*
   * We get the shortest path even when the shortest path has more
   * nodes than the minimum number of nodes between start and end
   */
  def testMoreNodes() {
    val locations = List(
      Map("startLocation" -> "Kruthika's abode", "endLocation" -> "Mark's crib", "distance" -> 1), // 1
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
      Map("startLocation" -> "Mark's crib", "endLocation" -> "Kirk's farm", "distance" -> 1),   // 2
      Map("startLocation" -> "Mark's crib", "endLocation" -> "Nathan's flat", "distance" -> 12),
      Map("startLocation" -> "Bryce's den", "endLocation" -> "Craig's haunt", "distance" -> 10),
      Map("startLocation" -> "Bryce's den", "endLocation" -> "Mike's digs", "distance" -> 9),
      Map("startLocation" -> "Mike's digs", "endLocation" -> "Cam's dwelling", "distance" -> 1), // 5
      Map("startLocation" -> "Mike's digs", "endLocation" -> "Nathan's flat", "distance" -> 1),  // 4
      Map("startLocation" -> "Cam's dwelling", "endLocation" -> "Craig's haunt", "distance" -> 1), // 6/7
      Map("startLocation" -> "Nathan's flat", "endLocation" -> "Kirk's farm", "distance" -> 1)  // 3
    )

    var (start, end) = ("Kruthika's abode", "Craig's haunt")
    var (d, p) = ShortPath.shortestPath(ShortPath.dijkstra(ShortPath.listToGraph(locations), start, end), start, end)
    Assert.assertTrue(p.size == 7)
  }
  
  /*
   * Distances in millions of miles. No specific edge case here. 
   */
  def testPlanets(){
    val locations = List(
      Map("startLocation" -> "Mercury", "endLocation" -> "Venus", "distance" -> 31),
      Map("startLocation" -> "Mercury", "endLocation" -> "Earth", "distance" -> 57),
      Map("startLocation" -> "Mercury", "endLocation" -> "Mars", "distance" -> 105),
      Map("startLocation" -> "Mercury", "endLocation" -> "Jupiter", "distance" -> 447),
      Map("startLocation" -> "Mercury", "endLocation" -> "Uranus", "distance" -> 1743),
      Map("startLocation" -> "Venus", "endLocation" -> "Earth", "distance" -> 26),
      Map("startLocation" -> "Venus", "endLocation" -> "Mars", "distance" -> 74),
      Map("startLocation" -> "Venus", "endLocation" -> "Jupiter", "distance" -> 416),
      Map("startLocation" -> "Venus", "endLocation" -> "Saturn", "distance" -> 820),
      Map("startLocation" -> "Venus", "endLocation" -> "Uranus", "distance" -> 1712),
      Map("startLocation" -> "Earth", "endLocation" -> "Mars", "distance" -> 48),
      Map("startLocation" -> "Earth", "endLocation" -> "Jupiter", "distance" -> 390),
      Map("startLocation" -> "Earth", "endLocation" -> "Saturn", "distance" -> 794),
      Map("startLocation" -> "Earth", "endLocation" -> "Uranus", "distance" -> 1686),
      Map("startLocation" -> "Mars", "endLocation" -> "Jupiter", "distance" -> 342),
      Map("startLocation" -> "Mars", "endLocation" -> "Saturn", "distance" -> 746),
      Map("startLocation" -> "Mars", "endLocation" -> "Uranus", "distance" -> 1638),
      Map("startLocation" -> "Jupiter", "endLocation" -> "Uranus", "distance" -> 1296),
      Map("startLocation" -> "Jupiter", "endLocation" -> "Neptune", "distance" -> 2311),
      Map("startLocation" -> "Saturn", "endLocation" -> "Uranus", "distance" -> 892),
      Map("startLocation" -> "Saturn", "endLocation" -> "Neptune", "distance" -> 1907),
      Map("startLocation" -> "Uranus", "endLocation" -> "Neptune", "distance" -> 1015)
    )
    
    var (start, end) = ("Mercury", "Neptune")
    var (d, p) = ShortPath.shortestPath(ShortPath.dijkstra(ShortPath.listToGraph(locations), start, end), start, end)
    Assert.assertTrue(p.size == 3)
  }

  /*
   * Distances in miles. Just another example.
   */
    def testCities(){
    val locations = List(
      Map("startLocation" -> "NYC", "endLocation" -> "Detroit", "distance" -> 615),
      Map("startLocation" -> "NYC", "endLocation" -> "Toronto", "distance" -> 491),
      Map("startLocation" -> "NYC", "endLocation" -> "Pittsburgh", "distance" -> 369),
      Map("startLocation" -> "Pittsburgh", "endLocation" -> "Detroit", "distance" -> 286),
      Map("startLocation" -> "Pittsburgh", "endLocation" -> "Indianapolis", "distance" -> 360),
      Map("startLocation" -> "Indianapolis", "endLocation" -> "Chicago", "distance" -> 183),
      Map("startLocation" -> "Detroit", "endLocation" -> "Chicago", "distance" -> 283),
      Map("startLocation" -> "Detroit", "endLocation" -> "Sault Ste Marie", "distance" -> 346),
      Map("startLocation" -> "Sault Ste Marie", "endLocation" -> "Toronto", "distance" -> 432),
      Map("startLocation" -> "Sault Ste Marie", "endLocation" -> "Milwaukee", "distance" -> 399),
      Map("startLocation" -> "Chicago", "endLocation" -> "Milwaukee", "distance" -> 92)
    )
    var (start, end) = ("NYC", "Milwaukee")
     var (d, p) = ShortPath.shortestPath(ShortPath.dijkstra(ShortPath.listToGraph(locations), start, end), start, end)
    Assert.assertEquals(p, Buffer("NYC", "Detroit", "Chicago", "Milwaukee"))
  }
  
}