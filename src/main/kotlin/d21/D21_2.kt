package d21

import d18.set
import input.Input

fun simulate(lines: List<String>, steps: Int, start: Pair<Int, Int>): Int {
    val countEvens = (start.first + start.second + steps) % 2 == 0

    val markedLines = bfs(lines, steps, start)

    return count(markedLines, countEvens)
}

fun main() {
    val totalSteps = 26501365L

    val rawLines = Input.read("input.txt")

    val middle = rawLines.size / 2
    val end = rawLines.lastIndex
    val mapsToEdge = totalSteps / rawLines.size

    val lines = rawLines.mapIndexed { index, line -> if (index == middle) line.set(middle, '.') else line }

    val fullOdd = simulate(lines, end * 2 + 1, Pair(middle, middle))
    val fullEven = simulate(lines, end * 2, Pair(middle, middle))

    val topLeftCorner = simulate(lines, middle - 1, Pair(end, end))
    val topRightCorner = simulate(lines, middle - 1, Pair(end, 0))
    val bottomLeftCorner = simulate(lines, middle - 1, Pair(0, end))
    val bottomRightCorner = simulate(lines, middle - 1, Pair(0, 0))

    val topLeftCutCorner = simulate(lines, end + middle, Pair(end, end))
    val topRightCutCorner = simulate(lines, end + middle, Pair(end, 0))
    val bottomLeftCutCorner = simulate(lines, end + middle, Pair(0, end))
    val bottomRightCutCorner = simulate(lines, end + middle, Pair(0, 0))

    val topArrow = simulate(lines, end, Pair(end, middle))
    val rightArrow = simulate(lines, end, Pair(middle, 0))
    val bottomArrow = simulate(lines, end, Pair(0, middle))
    val leftArrow = simulate(lines, end, Pair(middle, end))

    var even = totalSteps % 2 == 0L
    var sumOfInterior = 0L + if (even) fullEven else fullOdd

    for (i in 1..<mapsToEdge) {
        even = !even
        sumOfInterior += 4 * i * if (even) fullEven else fullOdd
    }

    val sumOfArrows = topArrow + rightArrow + bottomArrow + leftArrow

    val sumOfCutCorners = (topLeftCutCorner + topRightCutCorner + bottomLeftCutCorner + bottomRightCutCorner) *
            (mapsToEdge - 1)

    val sumOfCorners = (topLeftCorner + topRightCorner + bottomLeftCorner + bottomRightCorner) *
            mapsToEdge

    val total = sumOfInterior + sumOfArrows + sumOfCutCorners + sumOfCorners
    println(total)
}