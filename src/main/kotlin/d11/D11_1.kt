package d11

import input.Input
import kotlin.math.abs

fun expandUniverse(universe: List<String>): List<String> {
    var expanded = universe.map { "" }.toMutableList()

    // expand columns
    for (colI in universe[0].indices) {
        val col = universe.map { it[colI] }.joinToString("")
        expanded = if (col.any { it == '#' }) {
            expanded
                .mapIndexed { index, row -> row + universe[index][colI] }
                .toMutableList()
        } else {
            expanded
                .mapIndexed { index, row -> row + universe[index][colI] + universe[index][colI] }
                .toMutableList()
        }
    }

    // expand rows
    for (rowI in universe.lastIndex downTo 0) {
        val row = universe[rowI]
        if (row.all { it == '.' }) {
            expanded.add(rowI, expanded[rowI])
        }
    }

    return expanded
}

fun findDistance(g1: Galaxy, g2: Galaxy): Int {
    val loc1 = g1.loc
    val loc2 = g2.loc
    return abs(loc1.first - loc2.first) + abs(loc1.second - loc2.second)
}

fun main() {
    val lines = Input.read("input.txt")
    val universe = expandUniverse(lines)

    val galaxies = findGalaxies(universe)

    val pairs = findPairs(galaxies)

    var sum = 0
    pairs.forEach {
        sum += findDistance(it.first, it.second)
    }
    println(sum)
}