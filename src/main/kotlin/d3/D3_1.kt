package d3

import input.Input

fun sumOfLine(lines: List<String>, lineIndex: Int): Int {
    val line = lines[lineIndex]

    var sum = 0

    var i = 0
    while (i < line.length) {
        val c = line[i]
        if (!c.isDigit()) {
            i++
            continue
        }

        val numberStartIndex = i
        while (i < line.length && line[i].isDigit()) i++
        val numberEndIndex = i - 1

        if (checkForSymbol(lines, lineIndex, numberStartIndex, numberEndIndex)) {
            val numberValue = line.substring(numberStartIndex, numberEndIndex + 1).toInt()
            sum += numberValue
        }

        i++
    }

    return sum
}

fun checkForSymbol(lines: List<String>, lineIndex: Int, startIndex: Int, endIndex: Int): Boolean {
    val line = lines[lineIndex]

    if (startIndex > 0 && isSymbol(line[startIndex - 1])) return true
    if (endIndex < line.length - 1 && isSymbol(line[endIndex + 1])) return true

    if (lineIndex > 0) {
        val previousLine = lines[lineIndex - 1]

        if (startIndex > 0 && isSymbol(previousLine[startIndex - 1])) return true
        if (containsSymbol(previousLine.substring(startIndex, endIndex + 1))) return true
        if (endIndex < previousLine.length - 1 && isSymbol(previousLine[endIndex + 1])) return true
    }

    if (lineIndex < lines.size - 1) {
        val nextLine = lines[lineIndex + 1]

        if (startIndex > 0 && isSymbol(nextLine[startIndex - 1])) return true
        if (containsSymbol(nextLine.substring(startIndex, endIndex + 1))) return true
        if (endIndex < nextLine.length - 1 && isSymbol(nextLine[endIndex + 1])) return true
    }

    return false
}

fun containsSymbol(s: String): Boolean {
    return s.any { isSymbol(it) }
}

fun isSymbol(c: Char): Boolean {
    return c != '.'
}

fun main() {
    val lines = Input.read("input.txt")
    println(lines.indices.sumOf { sumOfLine(lines, it) })
}