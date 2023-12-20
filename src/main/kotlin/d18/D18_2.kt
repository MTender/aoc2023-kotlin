package d18

import d16.Direction
import input.Input
import kotlin.math.abs

val INT_TO_DIR = mapOf(
    Pair(0, Direction.RIGHT),
    Pair(2, Direction.LEFT),
    Pair(3, Direction.ABOVE),
    Pair(1, Direction.BELOW)
)

data class BigInstruction(
    val dir: Direction,
    val count: Long
)

fun parseRealInput(lines: List<String>): List<BigInstruction> {
    return lines.map {
        val parts = it.split(" ")
        val hex = parts[2].replace("[()#]".toRegex(), "")

        val dir = INT_TO_DIR[hex[5].digitToInt()]!!
        val countStr = hex.substring(0, 5)

        BigInstruction(
            dir,
            countStr.toLong(16)
        )
    }
}

fun shoelaceArea(coordinates: List<Pair<Long, Long>>): Long {
    var sum = 0L
    for (i in 1..coordinates.lastIndex) {
        val c0 = coordinates[i - 1]
        val c1 = coordinates[i]

        val p = c0.first * c1.second - c0.second * c1.first
        sum += p
    }
    return abs(sum / 2)
}

fun main() {
    val lines = Input.read("input.txt")
    val instructions = parseRealInput(lines)

    var previous = Pair(0L, 0L)
    val coordinates = mutableListOf(previous)

    for (instruction in instructions) {
        val next = when (instruction.dir) {
            Direction.LEFT -> Pair(previous.first, previous.second - instruction.count)
            Direction.RIGHT -> Pair(previous.first, previous.second + instruction.count)
            Direction.BELOW -> Pair(previous.first + instruction.count, previous.second)
            Direction.ABOVE -> Pair(previous.first - instruction.count, previous.second)
        }

        previous = next.copy()
        coordinates.add(next)
    }

    val length = instructions.sumOf { it.count }

    val shoelaceArea = shoelaceArea(coordinates)

    val area = shoelaceArea + length / 2 + 1
    println(area)
}