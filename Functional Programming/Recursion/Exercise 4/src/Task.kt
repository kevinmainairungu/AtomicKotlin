package recursionExercise4

import atomictest.eq

class City(val name: String) {
  val connections = mutableListOf<City>()

  override fun toString() = "($name, " +
    "connections: ${connections.map { it.name }})"
}

fun addConnection(first: City, second: City) {
  first.connections += second
  second.connections += first
}

fun City.getAllReachableRecursive(): Set<City> {
  // stores all the cities reachable from the the given city:
  val connections = mutableSetOf<City>()

  // add all connections reachable
  // from 'current' city to 'connections' set
  fun addConnectionsFrom(current: City) {
    TODO()
  }

  addConnectionsFrom(this)
  return connections
}

fun City.getAllReachableIterative(): Set<City> {
  // stores all the cities reachable from the given city
  val connections = mutableSetOf<City>()
  // stores all the cities which direct connections are to be analyzed
  val toBeAnalyzed = mutableSetOf(this)

  // for each 'current' city from 'toBeAnalyzed' set:
  // unmark it (remove from 'to be analyzed' set)
  // if it's already in 'connections', do nothing
  // add it to 'connections'
  // mark all its connections as 'to be analyzed'
  while (toBeAnalyzed.isNotEmpty()) {
    TODO()
  }
  return connections
}

fun main() {
  val dublin = City("Dublin")
  val liverpool = City("Liverpool")
  val manchester = City("Manchester")
  val leeds = City("Leeds")

  addConnection(liverpool, manchester)
  addConnection(manchester, leeds)

  liverpool.getAllReachableRecursive() eq
    setOf(liverpool, manchester, leeds)
  dublin.getAllReachableRecursive() eq
    setOf(dublin)

  liverpool.getAllReachableIterative() eq
    setOf(liverpool, manchester, leeds)
  dublin.getAllReachableIterative() eq
    setOf(dublin)
}