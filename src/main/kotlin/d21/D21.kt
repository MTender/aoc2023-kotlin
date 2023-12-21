package d21

import d18.set

data class Position(
    val loc: Pair<Int, Int>,
    val steps: Int
)

fun findStart(lines: List<String>): Pair<Int, Int> {
    for (i in lines.indices) {
        for (j in lines[i].indices) {
            if (lines[i][j] == 'S') return Pair(i, j)
        }
    }
    throw RuntimeException("could not find start")
}

fun List<String>.get(loc: Pair<Int, Int>): Char? {
    if (
        loc.first < 0 ||
        loc.first > this.lastIndex ||
        loc.second < 0 ||
        loc.second > this[0].lastIndex
    ) return null

    return this[loc.first][loc.second]
}

fun getNeighbors(lines: List<String>, pos: Position): List<Position> {
    val neighborLocations = listOf(
        Pair(pos.loc.first - 1, pos.loc.second),
        Pair(pos.loc.first + 1, pos.loc.second),
        Pair(pos.loc.first, pos.loc.second - 1),
        Pair(pos.loc.first, pos.loc.second + 1)
    )

    return neighborLocations
        .filter { lines.get(it) == '.' }
        .map { Position(it, pos.steps + 1) }
}

fun bfs(lines: List<String>, steps: Int, start: Pair<Int, Int>): List<String> {
    val markedLines = lines.toMutableList()

    val q = ArrayDeque<Position>()
    q.add(Position(start, 0))

    while (q.isNotEmpty()) {
        val pos = q.removeFirst()
        if (markedLines.get(pos.loc) == '0') continue

        markedLines[pos.loc.first] = markedLines[pos.loc.first].set(pos.loc.second, '0')

        if (pos.steps < steps) {
            val neighbors = getNeighbors(markedLines, pos)
            q.addAll(neighbors)
        }
    }

    return markedLines
}

fun count(markedLines: List<String>, countEvens: Boolean): Int {
    val visualization = markedLines.toMutableList()

    var count = 0
    for (i in markedLines.indices) {
        for (j in markedLines[i].indices) {
            if (markedLines[i][j] == '0') {
                if ((i + j) % 2 == 0) {
                    if (countEvens) count++
                    else visualization[i] = visualization[i].set(j, '.')
                } else if ((i + j) % 2 != 0) {
                    if (!countEvens) count++
                    else visualization[i] = visualization[i].set(j, '.')
                }
            }
        }
    }

    return count
}