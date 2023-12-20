package d4

import input.Input

fun main() {
    val lines = Input.read("input.txt")

    val cardMatches = lines
        .map { it.replace("\\s+".toRegex(), " ") }
        .map { getMatchesCount(it) }
        .mapIndexed { index, matchesCount -> Pair(index, matchesCount) }
        .toMap()

    val cardCounts = mutableMapOf<Int, Int>()
    cardMatches.keys.forEach { cardCounts[it] = 1 }

    for ((cardId, count) in cardCounts) {
        val matchesCount = cardMatches[cardId]!!
        if (matchesCount == 0) continue

        for (targetCardId in (cardId + 1..cardId + matchesCount)) {
            cardCounts[targetCardId] = cardCounts[targetCardId]!! + count
        }
    }

    val totalCards = cardCounts.values.sum()
    println(totalCards)
}