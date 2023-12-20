package d16

import input.Input

fun main() {
    val lines = Input.read("input.txt")
    val contraption = parseInput(lines)

    var best = -1

    for (row in contraption.indices) {
        val leftEnergized = testEntry(contraption, row, 0, Direction.LEFT)
        val rightEnergized = testEntry(contraption, row, contraption[row].lastIndex, Direction.RIGHT)

        if (leftEnergized > best) best = leftEnergized
        if (rightEnergized > best) best = rightEnergized
    }

    for (col in contraption[0].indices) {
        val topEnergized = testEntry(contraption, 0, col, Direction.ABOVE)
        val bottomEnergized = testEntry(contraption, contraption.lastIndex, col, Direction.BELOW)

        if (topEnergized > best) best = topEnergized
        if (bottomEnergized > best) best = bottomEnergized
    }

    println(best)
}

fun testEntry(contraption: List<List<Tile>>, row: Int, col: Int, from: Direction): Int {
    move(contraption, row, col, from)
    val count = countEnergized(contraption)
    resetContraption(contraption)
    return count
}

fun resetContraption(contraption: List<List<Tile>>) {
    for (line in contraption) {
        for (tile in line) {
            tile.energized = false
            tile.incomingLightDirections.clear()
        }
    }
}