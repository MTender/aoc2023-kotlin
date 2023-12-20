package d11

import input.Input
import kotlin.math.abs

fun findEmptyRows(universe: List<String>): List<Int> {
    val empty = mutableListOf<Int>()
    for (i in universe.indices) {
        if (universe[i].all { it == '.' }) {
            empty.add(i)
        }
    }
    return empty
}

fun findEmptyCols(universe: List<String>): List<Int> {
    val empty = mutableListOf<Int>()
    for (i in universe[0].indices) {
        val col = universe.map { it[i] }.joinToString("")
        if (col.all { it == '.' }) {
            empty.add(i)
        }
    }
    return empty
}

fun findDistanceComplex(g1: Galaxy, g2: Galaxy, emptyRows: List<Int>, emptyCols: List<Int>): Long {
    val loc1 = g1.loc
    val loc2 = g2.loc
    var distance: Long = (abs(loc1.first - loc2.first) + abs(loc1.second - loc2.second)).toLong()

    emptyRows.forEach {
        if (it.between(loc1.first, loc2.first)) {
            distance += 999_999
        }
    }

    emptyCols.forEach {
        if (it.between(loc1.second, loc2.second)) {
            distance += 999_999
        }
    }

    return distance
}

fun Int.between(a: Int, b: Int): Boolean {
    return this in a..b || this in b..a
}

fun main() {
    val universe = Input.read("input.txt")
    val emptyRows = findEmptyRows(universe)
    val emptyCols = findEmptyCols(universe)

    val galaxies = findGalaxies(universe)

    val pairs = findPairs(galaxies)

    var sum = 0L
    pairs.forEach {
        sum += findDistanceComplex(it.first, it.second, emptyRows, emptyCols)
    }
    println(sum)
}