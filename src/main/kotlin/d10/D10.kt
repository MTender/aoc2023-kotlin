package d10

fun findConnections(diagram: List<String>, loc: Pair<Int, Int>): List<Pair<Int, Int>> {
    val here = diagram[loc.first][loc.second]

    return when (here) {
        'S' -> {
            val connections = mutableListOf<Pair<Int, Int>>()

            if ("F7|".contains(diagram[loc.first - 1][loc.second])) connections.add(Pair(loc.first - 1, loc.second))
            if ("LJ|".contains(diagram[loc.first + 1][loc.second])) connections.add(Pair(loc.first + 1, loc.second))
            if ("FL-".contains(diagram[loc.first][loc.second - 1])) connections.add(Pair(loc.first, loc.second - 1))
            if ("J7-".contains(diagram[loc.first][loc.second + 1])) connections.add(Pair(loc.first, loc.second + 1))

            connections
        }

        '-' -> {
            listOf(Pair(loc.first, loc.second - 1), Pair(loc.first, loc.second + 1))
        }

        '|' -> {
            listOf(Pair(loc.first - 1, loc.second), Pair(loc.first + 1, loc.second))
        }

        'F' -> {
            listOf(Pair(loc.first + 1, loc.second), Pair(loc.first, loc.second + 1))
        }

        '7' -> {
            listOf(Pair(loc.first + 1, loc.second), Pair(loc.first, loc.second - 1))
        }

        'L' -> {
            listOf(Pair(loc.first - 1, loc.second), Pair(loc.first, loc.second + 1))
        }

        'J' -> {
            listOf(Pair(loc.first - 1, loc.second), Pair(loc.first, loc.second - 1))
        }

        else -> throw RuntimeException("did not recognise pipe segment")
    }
}

fun padInput(lines: List<String>): MutableList<String> {
    val diagram = mutableListOf<String>()
    diagram.add(".".repeat(lines[0].length + 2))
    diagram.addAll(
        lines.map { ".$it." }
    )
    diagram.add(".".repeat(lines[0].length + 2))
    return diagram
}

fun findStart(diagram: List<String>): Pair<Int, Int> {
    for (i in diagram.indices) {
        for (j in diagram[i].indices) {
            if (diagram[i][j] == 'S') return Pair(i, j)
        }
    }
    throw RuntimeException("could not find start")
}