package d10

import input.Input

fun main() {
    val lines = Input.read("input.txt")

    val diagram = padInput(lines)
    val pipe = findPipe(diagram)

    val start = findStart(diagram)

    val startReplacement = startReplacement(start, findConnections(diagram, start))
    diagram[start.first] =
        diagram[start.first].replaceRange(start.second, start.second + 1, startReplacement.toString())

    val expandedDiagram = expand(diagram)

    expandedDiagram[start.first * 2 + 1] =
        expandedDiagram[start.first * 2 + 1].replaceRange(start.second * 2 + 1, start.second * 2 + 2, "S")
    val expandedPipe = findPipe(expandedDiagram)

    bfs(expandedDiagram, expandedPipe)

    val revertedDiagram = undoExpand(expandedDiagram)

    var enclosedCount = 0
    for (i in revertedDiagram.indices) {
        for (j in revertedDiagram[i].indices) {
            if (!pipe.contains(Pair(i, j)) && revertedDiagram[i][j] != '0') {
                enclosedCount++
            }
        }
    }

    println(enclosedCount)
}

fun startReplacement(start: Pair<Int, Int>, cons: List<Pair<Int, Int>>): Char {
    val con1 = cons[0]
    val con2 = cons[1]

    if (start.first == con1.first && start.first == con2.first) return '-'
    if (start.second == con1.second && start.second == con2.second) return '|'
    if (
        cons.contains(Pair(start.first, start.second - 1)) &&
        cons.contains(Pair(start.first - 1, start.second))
    ) return 'J'
    if (
        cons.contains(Pair(start.first, start.second - 1)) &&
        cons.contains(Pair(start.first + 1, start.second))
    ) return '7'
    if (
        cons.contains(Pair(start.first, start.second + 1)) &&
        cons.contains(Pair(start.first - 1, start.second))
    ) return 'L'
    if (
        cons.contains(Pair(start.first, start.second + 1)) &&
        cons.contains(Pair(start.first + 1, start.second))
    ) return 'F'

    throw RuntimeException("start does not have two connecting pipes")
}

fun findPipe(diagram: List<String>): Set<Pair<Int, Int>> {
    val start = findStart(diagram)

    val firstConnections = findConnections(diagram, start)

    var prev = start.copy()
    var loc = firstConnections[0].copy()

    val pipe = mutableSetOf<Pair<Int, Int>>()
    pipe.add(start)

    while (loc != start) {
        pipe.add(loc)
        val cons = findConnections(diagram, loc)
        val next = if (cons[0] == prev) cons[1] else cons[0]

        prev = loc.copy()
        loc = next.copy()
    }

    return pipe
}

fun undoExpand(diagram: MutableList<String>): MutableList<String> {
    val revertedDiagram = mutableListOf<String>()

    for (i in diagram.indices) {
        if (i % 2 == 0) continue

        var line = ""
        for (j in diagram[i].indices) {
            if (j % 2 == 0) continue

            line += diagram[i][j]
        }

        revertedDiagram.add(line)
    }

    return revertedDiagram
}

fun expand(diagram: MutableList<String>): MutableList<String> {
    val expandedDiagram = mutableListOf<String>()
    for (line in diagram) {
        expandedDiagram.addAll(expandLine(line))
    }
    return expandedDiagram
}

fun expandLine(line: String): List<String> {
    var prevLine = ""
    var newLine = ""

    for (c in line) {
        prevLine += "."
        prevLine += if ("LJ|".contains(c)) "|" else "."
        newLine += if ("J7-".contains(c)) "-" else "."
        newLine += c
    }

    return listOf(prevLine, newLine)
}

fun bfs(diagram: MutableList<String>, pipe: Set<Pair<Int, Int>>) {
    val q = ArrayDeque<Pair<Int, Int>>()
    diagram[0] = diagram[0].replaceRange(0, 1, "0")
    q.add(Pair(0, 0))

    while (q.isNotEmpty()) {
        val loc = q.removeLast()

        val surrounding = getSurrounding(diagram, loc)

        for (sLoc in surrounding) {
            if (!pipe.contains(sLoc) && diagram[sLoc.first][sLoc.second] != '0') {
                diagram[sLoc.first] = diagram[sLoc.first].replaceRange(sLoc.second, sLoc.second + 1, "0")
                q.add(sLoc)
            }
        }
    }
}

fun getSurrounding(diagram: List<String>, loc: Pair<Int, Int>): List<Pair<Int, Int>> {
    val surrounding = mutableListOf<Pair<Int, Int>>()

    if (loc.first > 0) {
        if (loc.second > 0) surrounding.add(Pair(loc.first - 1, loc.second - 1))
        surrounding.add(Pair(loc.first - 1, loc.second))
        if (loc.second < diagram[loc.first].lastIndex) surrounding.add(Pair(loc.first - 1, loc.second + 1))
    }

    if (loc.second > 0) surrounding.add(Pair(loc.first, loc.second - 1))
    if (loc.second < diagram[loc.first].lastIndex) surrounding.add(Pair(loc.first, loc.second + 1))

    if (loc.first < diagram.lastIndex) {
        if (loc.second > 0) surrounding.add(Pair(loc.first + 1, loc.second - 1))
        surrounding.add(Pair(loc.first + 1, loc.second))
        if (loc.second < diagram[loc.first].lastIndex) surrounding.add(Pair(loc.first + 1, loc.second + 1))
    }

    return surrounding
}