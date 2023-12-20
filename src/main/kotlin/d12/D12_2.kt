package d12

import input.Input

fun parseInput(lines: List<String>, repeat: Int): List<SpringRow> {
    return lines.map { line ->
        val parts = line.split(" ")

        val springs = parts[0]
            .repeat(repeat, "?")
            .trim { it == '.' }
            .replace("\\.+".toRegex(), ".")

        val groups = parts[1]
            .split(",")
            .map { it.toInt() }
            .repeat(repeat)
        SpringRow(springs, groups)
    }
}

fun main() {
    val lines = Input.read("input.txt")
    val springRows = parseInput(lines, 5)

    var count = 0L

    for (row in springRows) {
        val memo = mutableMapOf<Triple<Int, Int, Int>, Long>()
        val springs = row.springs + "."
        val groups = row.groups.toMutableList()
        groups.add(0)

        val d12part2 = D12part2(memo, springs, groups)

        val rowCount = d12part2.getOptionsCountMemo(
            "",
            0,
            0,
            groups[0]
        )
        count += rowCount
    }

    println(count)
}

fun String.repeat(n: Int, separator: String): String {
    if (n < 1) return ""
    return this + "$separator$this".repeat(n - 1)
}

fun <T> List<T>.repeat(n: Int): List<T> {
    var res = this
    (2..n).forEach { _ -> res = res + this }
    return res
}