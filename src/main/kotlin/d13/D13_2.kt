package d13

import input.Input

fun findDoubleRowsWithSmudge(pattern: List<String>): List<Int> {
    val doubleRows = mutableListOf<Int>()
    for (i in 1..<pattern.size) {
        if (countMismatches(pattern[i], pattern[i - 1]) > 1) continue

        doubleRows.add(i)
    }
    return doubleRows
}

fun findDoubleColsWithSmudge(pattern: List<String>): List<Int> {
    val doubleCols = mutableListOf<Int>()
    for (i in 1..<pattern[0].length) {
        if (countMismatches(getCol(pattern, i), getCol(pattern, i - 1)) > 1) continue

        doubleCols.add(i)
    }
    return doubleCols
}

fun countMismatches(s1: String, s2: String): Int {
    return s1.mapIndexed { index, c -> c != s2[index] }.count { it }
}

fun isHorizontalEdgeMirrorWithSmudge(pattern: List<String>, rowI: Int): Boolean {
    var upper = rowI - 1
    var lower = rowI
    var markedSmudges = 0

    while (0 <= upper && lower <= pattern.lastIndex) {
        val mismatches = countMismatches(pattern[upper--], pattern[lower++])
        if (markedSmudges + mismatches > 1) return false

        markedSmudges += mismatches
    }
    return markedSmudges == 1
}

fun isVerticalEdgeMirrorWithSmudge(pattern: List<String>, colI: Int): Boolean {
    var left = colI - 1
    var right = colI
    var markedSmudges = 0

    while (0 <= left && right <= pattern[0].lastIndex) {
        val mismatches = countMismatches(getCol(pattern, left--), getCol(pattern, right++))
        if (markedSmudges + mismatches > 1) return false

        markedSmudges += mismatches
    }
    return markedSmudges == 1
}

fun checkForHorizontalWithSmudge(pattern: List<String>): Int {
    val doubleRowsWithSmudge = findDoubleRowsWithSmudge(pattern)
    if (doubleRowsWithSmudge.isEmpty()) return -1

    for (row in doubleRowsWithSmudge) {
        if (isHorizontalEdgeMirrorWithSmudge(pattern, row)) return row
    }

    return -1
}

fun checkForVerticalWithSmudge(pattern: List<String>): Int {
    val doubleColsWithSmudge = findDoubleColsWithSmudge(pattern)
    if (doubleColsWithSmudge.isEmpty()) return -1

    for (col in doubleColsWithSmudge) {
        if (isVerticalEdgeMirrorWithSmudge(pattern, col)) return col
    }

    return -1
}

fun main() {
    val lines = Input.read("input.txt")
    val patterns = parseInput(lines)

    var sum = 0

    for (pattern in patterns) {
        val rowIndex = checkForHorizontalWithSmudge(pattern)
        if (rowIndex != -1) {
            sum += rowIndex * 100
            continue
        }

        val colIndex = checkForVerticalWithSmudge(pattern)
        if (colIndex != -1) {
            sum += colIndex
            continue
        }

        throw RuntimeException("no mirror found")
    }

    println(sum)
}