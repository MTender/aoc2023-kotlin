package d23

fun <K> MutableMap<K, Int>.putIfLargerOrAbsent(key: K, value: Int) {
    val current = this[key] ?: Int.MIN_VALUE

    if (current < value) this[key] = value
}

operator fun List<String>.get(loc: Pair<Int, Int>): Char {
    return this[loc.first][loc.second]
}

data class Move(
    val from: Pair<Int, Int>,
    val to: Pair<Int, Int>,
    val steps: Int
)

fun getSurrounding(loc: Pair<Int, Int>): List<Pair<Int, Int>> {
    return listOf(
        Pair(loc.first - 1, loc.second),
        Pair(loc.first + 1, loc.second),
        Pair(loc.first, loc.second - 1),
        Pair(loc.first, loc.second + 1)
    )
}