package d13

fun parseInput(lines: List<String>): List<List<String>> {
    return lines.split { it.isEmpty() }
}

fun equalRows(pattern: List<String>, i1: Int, i2: Int): Boolean {
    return pattern[i1] == pattern[i2]
}

fun equalCols(pattern: List<String>, i1: Int, i2: Int): Boolean {
    return pattern.all { it[i1] == it[i2] }
}

fun getCol(pattern: List<String>, i: Int): String {
    return pattern.map { it[i] }.joinToString("")
}

fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> {
    val idx = this.indexOfFirst(predicate)
    return if (idx == -1) {
        listOf(this)
    } else {
        return listOf(this.take(idx)) + this.drop(idx + 1).split(predicate)
    }
}

fun findDoubleRows(pattern: List<String>): List<Int> {
    val doubleRows = mutableListOf<Int>()
    for (i in 1..<pattern.size) {
        if (pattern[i] != pattern[i - 1]) continue

        doubleRows.add(i)
    }
    return doubleRows
}

fun findDoubleCols(pattern: List<String>): List<Int> {
    val doubleCols = mutableListOf<Int>()
    for (i in 1..<pattern[0].length) {
        if (getCol(pattern, i) != getCol(pattern, i - 1)) continue

        doubleCols.add(i)
    }
    return doubleCols
}