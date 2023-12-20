package d7

import input.Input

const val CARD_LABELS_JOKER = "AKQT98765432J"

data class JokerHand(
    val cards: String,
    val bid: Int
) : Comparable<JokerHand> {
    override fun compareTo(other: JokerHand): Int {
        val typeDiff = this.getTypeStrength() - other.getTypeStrength()
        if (typeDiff != 0) return typeDiff

        for (i in 0..4) {
            if (this.cards[i] != other.cards[i]) {
                return CARD_LABELS_JOKER.indexOf(other.cards[i]) - CARD_LABELS_JOKER.indexOf(this.cards[i])
            }
        }

        return 0
    }

    private fun getTypeStrength(): Int {
        val charCounts = mutableMapOf<Char, Int>()
        for (c in cards) {
            charCounts[c] = (charCounts[c] ?: 0) + 1
        }

        val jokersCount = charCounts['J'] ?: 0

        return when (charCounts.size) {
            5 -> {
                if (jokersCount != 0) return 2 // one pair
                return 1 // high card
            }

            4 -> {
                when (jokersCount) {
                    2, 1 -> 4 // three of a kind
                    0 -> 2 // one pair
                    else -> throw RuntimeException("impossible")
                }
            }

            3 -> {
                when (jokersCount) {
                    3, 2 -> 6 // four of a kind
                    1 -> {
                        if (charCounts.values.toSet() == setOf(2, 2, 1)) {
                            return 5 // full house
                        }
                        return 6 // four of a kind
                    }

                    0 -> {
                        if (charCounts.values.toSet() == setOf(2, 2, 1)) {
                            return 3 // two pair
                        }
                        return 4 // three of a kind
                    }

                    else -> throw RuntimeException("impossible")
                }
            }

            2 -> {
                if (jokersCount != 0) return 7 // five of a kind

                if (charCounts.values.toSet() == setOf(3, 2)) {
                    return 5 // full house
                }
                return 6 // four of a kind
            }

            1 -> 7 // five of a kind
            else -> {
                throw RuntimeException("impossible")
            }
        }
    }
}

fun parseJokerInput(lines: List<String>): List<JokerHand> {
    return lines
        .map { it.split(" ") }
        .map { JokerHand(it[0], it[1].toInt()) }
}

fun main() {
    val lines = Input.read("input.txt")

    val hands = parseJokerInput(lines)

    val sortedHands = hands.sorted()

    println(sortedHands.mapIndexed { index, hand -> hand.bid * (index + 1) }.sum())
}