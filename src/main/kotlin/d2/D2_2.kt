package d2

import input.Input

fun main() {
    val lines = Input.read("input.txt")
    val games = lines.map { parseGame(it) }

    val minGames = games.map { game ->
        val redMin = game.sets.maxOfOrNull { it.first } ?: 0
        val greenMin = game.sets.maxOfOrNull { it.second } ?: 0
        val blueMin = game.sets.maxOfOrNull { it.third } ?: 0

        Triple(redMin, greenMin, blueMin)
    }

    println(minGames.sumOf { it.toList().product() })
}

fun Collection<Int>.product(): Int {
    return this.reduce { acc, n -> acc * n }
}