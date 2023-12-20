package d3

import input.Input

fun findGears(lines: List<String>): List<Pair<Int, Int>> {
    val gears = mutableListOf<Pair<Int, Int>>()

    for (i in lines.indices) {
        for (j in lines[i].indices) {
            val c = lines[i][j]
            if (c != '*') continue

            gears.add(Pair(i, j))
        }
    }

    return gears
}

fun findAdjacentNumbers(lines: List<String>, gear: Pair<Int, Int>): List<Int> {
    val numbers = mutableListOf<Int>()

    val line = lines[gear.first]

    if (gear.first > 0 && !lines[gear.first - 1][gear.second].isDigit()) {
        val previousLine = lines[gear.first - 1]

        numbers.addAll(getNumbersNextToLoc(previousLine, gear.second))
    } else {
        val previousLine = lines[gear.first - 1]

        if (previousLine[gear.second].isDigit()) {
            numbers.add(getNumberAt(previousLine, gear.second))
        }
    }

    numbers.addAll(getNumbersNextToLoc(line, gear.second))

    if (gear.first < lines.size - 1 && !lines[gear.first + 1][gear.second].isDigit()) {
        val nextLine = lines[gear.first + 1]

        numbers.addAll(getNumbersNextToLoc(nextLine, gear.second))
    } else {
        val nextLine = lines[gear.first + 1]

        if (nextLine[gear.second].isDigit()) {
            numbers.add(getNumberAt(nextLine, gear.second))
        }
    }

    return numbers
}

fun getNumbersNextToLoc(line: String, col: Int): List<Int> {
    val numbers = mutableListOf<Int>()

    if (
        col > 0 &&
        line[col - 1].isDigit()
    ) {
        numbers.add(getNumberAt(line, col - 1))
    }

    if (
        col < line.length - 1 &&
        line[col + 1].isDigit()
    ) {
        numbers.add(getNumberAt(line, col + 1))
    }

    return numbers
}

fun getNumberAt(line: String, i: Int): Int {
    var start = i - 1
    while (start >= 0 && line[start].isDigit()) start--
    start++

    var end = i + 1
    while (end < line.length && line[end].isDigit()) end++
    end--

    return line.substring(start, end + 1).toInt()
}

fun main() {
    val lines = Input.read("input.txt")

    val gears = findGears(lines)

    var gearRatiosSum = 0

    for (gear in gears) {
        val numbers = findAdjacentNumbers(lines, gear)

        if (numbers.size != 2) continue

        gearRatiosSum += numbers[0] * numbers[1]
    }

    println(gearRatiosSum)
}