package d16

import input.Input

fun main() {
    val lines = Input.read("input.txt")
    val contraption = parseInput(lines)

    move(contraption, 0, 0, Direction.LEFT)

    val energized = countEnergized(contraption)
    println(energized)
}