package d18

import d10.getSurrounding

fun String.set(i: Int, c: Char): String {
    return this.replaceRange(i, i + 1, c.toString())
}

fun isOutOfBounds(lines: List<String>, loc: Pair<Int, Int>): Boolean {
    return loc.first < 0 ||
            loc.first >= lines.size ||
            loc.second < 0 ||
            loc.second >= lines[0].length
}

fun bfs(diagram: List<String>): List<String> {
    val copy = diagram.toMutableList()
    copy[0] = copy[0].set(0, '0')

    val q = ArrayDeque<Pair<Int, Int>>()
    q.add(Pair(0, 0))

    while (q.isNotEmpty()) {
        val loc = q.removeLast()

        val surrounding = getSurrounding(copy, loc)

        for (sLoc in surrounding) {
            if (copy[sLoc.first][sLoc.second] == '.') {
                copy[sLoc.first] = copy[sLoc.first].set(sLoc.second, '0')
                q.add(sLoc)
            }
        }
    }

    return copy
}