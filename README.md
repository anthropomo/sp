# sp
##Shortest Path Exercize

####External Dependencies

[JUnit](http://junit.org/)

####Running & Testing

- Clone repo.

- `cd` to project root `[...]/sb/shortpath`

- [`$ sbt`](http://www.scala-sbt.org)

- `> run`
```
[info] Compiling 1 Scala source to /Users/ml/git/sp/shortpath/target/scala-2.10/classes...
[info] Running main.scala.ShortPath 
Finding the Shortest Path
Input:
List(
	Map(distance -> 9, startLocation -> Kruthika's abode, endLocation -> Mark's crib)
	Map(distance -> 4, startLocation -> Kruthika's abode, endLocation -> Greg's casa)
	Map(distance -> 18, startLocation -> Kruthika's abode, endLocation -> Matt's pad)
	Map(distance -> 8, startLocation -> Kruthika's abode, endLocation -> Brian's apartment)
	Map(distance -> 7, startLocation -> Brian's apartment, endLocation -> Wesley's condo)
	Map(distance -> 17, startLocation -> Brian's apartment, endLocation -> Cam's dwelling)
	Map(distance -> 13, startLocation -> Greg's casa, endLocation -> Cam's dwelling)
	Map(distance -> 19, startLocation -> Greg's casa, endLocation -> Mike's digs)
	Map(distance -> 14, startLocation -> Greg's casa, endLocation -> Matt's pad)
	Map(distance -> 10, startLocation -> Wesley's condo, endLocation -> Kirk's farm)
	Map(distance -> 11, startLocation -> Wesley's condo, endLocation -> Nathan's flat)
	Map(distance -> 6, startLocation -> Wesley's condo, endLocation -> Bryce's den)
	Map(distance -> 19, startLocation -> Matt's pad, endLocation -> Mark's crib)
	Map(distance -> 15, startLocation -> Matt's pad, endLocation -> Nathan's flat)
	Map(distance -> 14, startLocation -> Matt's pad, endLocation -> Craig's haunt)
	Map(distance -> 9, startLocation -> Mark's crib, endLocation -> Kirk's farm)
	Map(distance -> 12, startLocation -> Mark's crib, endLocation -> Nathan's flat)
	Map(distance -> 10, startLocation -> Bryce's den, endLocation -> Craig's haunt)
	Map(distance -> 9, startLocation -> Bryce's den, endLocation -> Mike's digs)
	Map(distance -> 20, startLocation -> Mike's digs, endLocation -> Cam's dwelling)
	Map(distance -> 12, startLocation -> Mike's digs, endLocation -> Nathan's flat)
	Map(distance -> 18, startLocation -> Cam's dwelling, endLocation -> Craig's haunt)
	Map(distance -> 3, startLocation -> Nathan's flat, endLocation -> Kirk's farm)
)
Start point: Kruthika's abode
End point: Craig's haunt
Result: Map(distance -> 31, path -> Kruthika's abode => Brian's apartment => Wesley's condo => Bryce's den => Craig's haunt)
```
- `> test`
```
[info] Passed: Total 10, Failed 0, Errors 0, Passed 10
[success] Total time: 0 s, completed Feb 25, 2015 9:46:14 AM
```
