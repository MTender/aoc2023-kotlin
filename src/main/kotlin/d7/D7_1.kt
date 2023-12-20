package d7

import input.Input

const val CARD_LABELS = "AKQJT98765432"

data class Hand(
    val cards: String,
    val bid: Int
) : Comparable<Hand> {
    override fun compareTo(other: Hand): Int {
        val typeDiff = this.getTypeStrength() - other.getTypeStrength()
        if (typeDiff != 0) return typeDiff

        for (i in 0..4) {
            if (this.cards[i] != other.cards[i]) {
                return CARD_LABELS.indexOf(other.cards[i]) - CARD_LABELS.indexOf(this.cards[i])
            }
        }

        return 0
    }

    private fun getTypeStrength(): Int {
        val charCounts = mutableMapOf<Char, Int>()
        for (c in cards) {
            charCounts[c] = (charCounts[c] ?: 0) + 1
        }

        return when (charCounts.size) {
            5 -> 1 // high card
            4 -> 2 // one pair
            3 -> {
                if (charCounts.values.toSet() == setOf(2, 2, 1)) {
                    return 3 // two pair
                }
                return 4 // three of a kind
            }

            2 -> {
                if (charCounts.values.toSet() == setOf(3, 2)) {
                    return 5 // full house
                }
                return 6 // four of a kind
            }

            1 -> 7 // five of a kind
            else -> throw RuntimeException("impossible")
        }
    }
}

fun parseInput(lines: List<String>): List<Hand> {
    return lines
        .map { it.split(" ") }
        .map { Hand(it[0], it[1].toInt()) }
}

fun main() {
    val lines = Input.read("input.txt")

    val hands = parseInput(lines)

    val sortedHands = hands.sorted()

    println(sortedHands.mapIndexed { index, hand -> hand.bid * (index + 1) }.sum())
}