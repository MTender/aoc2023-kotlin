package d17

import java.util.*

abstract class Location(
    open val row: Int,
    open val col: Int
) {

    abstract fun getNeighbors(): List<Location>

    abstract fun isValidTarget(target: Pair<Int, Int>): Boolean
}

fun parseInput(lines: List<String>): List<List<Int>> {
    return lines.map { line -> line.toCharArray().map { it.digitToInt() } }
}

fun dijkstra(weights: List<List<Int>>, source: Location, target: Pair<Int, Int>): Int {
    val distances = mutableMapOf<Location, Double>()
    val previous = mutableMapOf<Location, Location?>()

    distances[source] = 0.0

    val settledVertices = mutableSetOf<Location>()
    val unsettledVertices = mutableSetOf(source)
    val unsettledQueue = PriorityQueue<Location>(
        compareBy { distances[it] }
    )
    unsettledQueue.add(source)

    while (unsettledQueue.isNotEmpty()) {
        val loc = unsettledQueue.remove()
        unsettledVertices.remove(loc)

        for (neighbor in loc.getNeighbors()) {
            if (isOutOfBounds(weights, neighbor)) continue // exclude out of bounds
            if (settledVertices.contains(neighbor)) continue // exclude settled

            val alt = distances[loc]!! + weights[neighbor.row][neighbor.col]
            if (alt < (distances[neighbor] ?: Double.POSITIVE_INFINITY)) {
                distances[neighbor] = alt
                previous[neighbor] = loc
                if (!unsettledVertices.contains(neighbor)) {
                    unsettledVertices.add(neighbor)
                    unsettledQueue.add(neighbor)
                }
            }
        }

        settledVertices.add(loc)
    }

    return distances
        .filter { it.key.isValidTarget(target) }
        .minBy { distances[it.key]!! }.value.toInt()
}

fun isOutOfBounds(weights: List<List<Int>>, loc: Location): Boolean {
    return loc.row < 0 ||
            loc.row >= weights.size ||
            loc.col < 0 ||
            loc.col >= weights[0].size
}