package d18

import d10.padInput
import d16.Direction
import input.Input

val CHAR_TO_DIR = mapOf(
    Pair('R', Direction.RIGHT),
    Pair('L', Direction.LEFT),
    Pair('U', Direction.ABOVE),
    Pair('D', Direction.BELOW)
)

data class Instruction(
    val dir: Direction,
    val count: Int
)

fun parseInput(lines: List<String>): List<Instruction> {
    return lines.map {
        val parts = it.split(" ")

        Instruction(
            CHAR_TO_DIR[parts[0][0]]!!,
            parts[1].toInt()
        )
    }
}

fun dig(instructions: List<Instruction>): List<String> {
    var terrain = mutableListOf("#")
    var loc = Pair(0, 0)

    for (instruction in instructions) {
        for (i in 1..instruction.count) {
            when (instruction.dir) {
                Direction.RIGHT -> {
                    loc = Pair(loc.first, loc.second + 1)

                    if (isOutOfBounds(terrain, loc)) {
                        terrain = terrain.map { "$it." }.toMutableList()
                    }
                }

                Direction.LEFT -> {
                    loc = Pair(loc.first, loc.second - 1)

                    if (isOutOfBounds(terrain, loc)) {
                        terrain = terrain.map { ".$it" }.toMutableList()
                        loc = Pair(loc.first, loc.second + 1) // new column means loc moves
                    }
                }

                Direction.BELOW -> {
                    loc = Pair(loc.first + 1, loc.second)

                    if (isOutOfBounds(terrain, loc)) {
                        terrain.add(".".repeat(terrain[0].length))
                    }
                }

                Direction.ABOVE -> {
                    loc = Pair(loc.first - 1, loc.second)

                    if (isOutOfBounds(terrain, loc)) {
                        terrain.add(0, ".".repeat(terrain[0].length))
                        loc = Pair(loc.first + 1, loc.second) // new row means loc moves
                    }
                }
            }

            terrain[loc.first] = terrain[loc.first].set(loc.second, '#')
        }
    }

    return terrain
}

fun main() {
    val lines = Input.read("input.txt")
    val instructions = parseInput(lines)

    val terrain = dig(instructions)

    val paddedTerrain = padInput(terrain)

    val floodedTerrain = bfs(paddedTerrain)

    val cubicMeters = floodedTerrain.joinToString("").count { it != '0' }
    println(cubicMeters)
}