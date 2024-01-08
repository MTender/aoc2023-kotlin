package d2

import input.Input

fun main() {
    val lines = Input.read("input.txt")
    val games = lines.map { parseGame(it) }

    val possibleGames = games.filter { game ->
        !(
                game.sets.map { it.first }.any { it > 12 } ||
                game.sets.map { it.second }.any { it > 13 } ||
                game.sets.map { it.third }.any { it > 14 }
        )
    }

    println(possibleGames.sumOf { it.id })
}