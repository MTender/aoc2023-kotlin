package d11

fun findGalaxies(universe: List<String>): List<Galaxy> {
    val galaxies = mutableListOf<Galaxy>()

    for (i in universe.indices) {
        for (j in universe[i].indices) {
            if (universe[i][j] != '#') continue

            val id = if (galaxies.lastIndex == -1) 1 else galaxies.last().id

            galaxies.add(Galaxy(id, Pair(i, j)))
        }
    }

    return galaxies
}

fun findPairs(galaxies: List<Galaxy>): List<Pair<Galaxy, Galaxy>> {
    val pairs = mutableListOf<Pair<Galaxy, Galaxy>>()

    for (i in galaxies.indices) {
        for (j in i + 1..galaxies.lastIndex) {
            pairs.add(Pair(galaxies[i], galaxies[j]))
        }
    }

    return pairs
}

data class Galaxy(
    val id: Int,
    val loc: Pair<Int, Int>
)