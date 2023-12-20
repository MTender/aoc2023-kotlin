package d17

import d16.Direction
import input.Input

data class Part1Location(
    override val row: Int,
    override val col: Int,
    val from: Direction,
    val straight: Int
) : Location(row, col) {

    override fun getNeighbors(): List<Location> {
        when (from) {
            Direction.LEFT -> {
                val neighbors = mutableListOf(
                    Part1Location(row - 1, col, Direction.BELOW, 1),
                    Part1Location(row + 1, col, Direction.ABOVE, 1)
                )
                if (straight < 3) neighbors.add(Part1Location(row, col + 1, from, straight + 1))
                return neighbors
            }

            Direction.RIGHT -> {
                val neighbors = mutableListOf(
                    Part1Location(row - 1, col, Direction.BELOW, 1),
                    Part1Location(row + 1, col, Direction.ABOVE, 1)
                )
                if (straight < 3) neighbors.add(Part1Location(row, col - 1, from, straight + 1))
                return neighbors
            }

            Direction.BELOW -> {
                val neighbors = mutableListOf(
                    Part1Location(row, col - 1, Direction.RIGHT, 1),
                    Part1Location(row, col + 1, Direction.LEFT, 1)
                )
                if (straight < 3) neighbors.add(Part1Location(row - 1, col, from, straight + 1))
                return neighbors
            }

            Direction.ABOVE -> {
                val neighbors = mutableListOf(
                    Part1Location(row, col - 1, Direction.RIGHT, 1),
                    Part1Location(row, col + 1, Direction.LEFT, 1)
                )
                if (straight < 3) neighbors.add(Part1Location(row + 1, col, from, straight + 1))
                return neighbors
            }
        }
    }

    override fun isValidTarget(target: Pair<Int, Int>): Boolean {
        return target == Pair(row, col)
    }
}

fun main() {
    val lines = Input.read("input.txt")
    val weights = parseInput(lines)

    val distance = dijkstra(
        weights,
        Part1Location(0, 0, Direction.LEFT, 0),
        Pair(weights.lastIndex, weights[0].lastIndex)
    )
    println(distance)
}