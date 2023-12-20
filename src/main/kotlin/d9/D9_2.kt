package d9

import input.Input

fun getPrevious(ints: List<Int>): Int {
    if (ints.all { it == 0 }) {
        return 0
    }

    val diffs = mutableListOf<Int>()
    for (i in 1..<ints.size) {
        diffs.add(ints[i] - ints[i - 1])
    }

    return ints[0] - getPrevious(diffs)
}

fun main() {
    val lines = Input.read("input.txt")

    val histories = parseInput(lines)

    val result = histories.sumOf { getPrevious(it) }
    println(result)
}