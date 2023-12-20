package d4

import input.Input
import kotlin.math.pow

fun getPoints(matchesCount: Int): Int {
    if (matchesCount == 0) return 0
    return (2.0.pow(matchesCount - 1)).toInt()
}

fun main() {
    val lines = Input.read("input.txt")

    println(
        lines
            .map { it.replace("\\s+".toRegex(), " ") }
            .map { getMatchesCount(it) }
            .sumOf { getPoints(it) }
    )
}