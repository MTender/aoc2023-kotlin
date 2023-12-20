package d13

import input.Input

fun isHorizontalEdgeMirror(pattern: List<String>, rowI: Int): Boolean {
    var upper = rowI - 2
    var lower = rowI + 1

    while (0 <= upper && lower <= pattern.lastIndex) {
        if (!equalRows(pattern, upper--, lower++)) return false
    }
    return true
}

fun isVerticalEdgeMirror(pattern: List<String>, colI: Int): Boolean {
    var left = colI - 2
    var right = colI + 1

    while (0 <= left && right <= pattern[0].lastIndex) {
        if (!equalCols(pattern, left--, right++)) return false
    }
    return true
}

fun checkForHorizontal(pattern: List<String>): Int {
    val doubleRows = findDoubleRows(pattern)
    if (doubleRows.isEmpty()) return -1

    for (row in doubleRows) {
        if (isHorizontalEdgeMirror(pattern, row)) return row
    }

    return -1
}

fun checkForVertical(pattern: List<String>): Int {
    val doubleCols = findDoubleCols(pattern)
    if (doubleCols.isEmpty()) return -1

    for (col in doubleCols) {
        if (isVerticalEdgeMirror(pattern, col)) return col
    }

    return -1
}

fun main() {
    val lines = Input.read("input.txt")
    val patterns = parseInput(lines)

    var sum = 0

    for (pattern in patterns) {
        val rowIndex = checkForHorizontal(pattern)
        if (rowIndex != -1) {
            sum += rowIndex * 100
            continue
        }

        val colIndex = checkForVertical(pattern)
        if (colIndex != -1) {
            sum += colIndex
            continue
        }

        throw RuntimeException("no mirror found")
    }

    println(sum)
}