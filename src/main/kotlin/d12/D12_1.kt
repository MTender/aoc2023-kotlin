package d12

import input.Input

fun parseInput(lines: List<String>): List<SpringRow> {
    return lines.map { line ->
        val parts = line.split(" ")
        val springs = parts[0].trim { it == '.' }.replace("\\.+".toRegex(), ".")
        val groups = parts[1].split(",").map { it.toInt() }
        SpringRow(springs, groups)
    }
}

fun permutations(current: String, active: Int, inactive: Int): List<String> {
    if (active < 0 || inactive < 0) return listOf()
    if (active == 0 && inactive == 0) return listOf(current)

    return permutations("$current.", active - 1, inactive) +
            permutations("$current#", active, inactive - 1)
}

fun replaceUnknowns(template: String, permutation: String): String {
    var result = template
    var pI = 0
    for (i in template.indices) {
        if (template[i] != '?') continue

        result = result.replaceRange(i, i + 1, permutation[pI++].toString())
    }
    return result
}

fun matches(row: String, groups: List<Int>): Boolean {
    val inactiveGroups = row.split("\\.+".toRegex()).toMutableList()
    inactiveGroups.removeIf { it.isEmpty() }

    for (i in inactiveGroups.indices) {
        if (inactiveGroups[i].length != groups[i]) return false
    }
    return true
}

fun main() {
    val lines = Input.read("input.txt")
    val springRows = parseInput(lines)

    var count = 0

    for (row in springRows) {
        val currentInactiveCount = row.springs.count { it == '#' }
        val unknownCount = row.springs.count { it == '?' }

        val requiredInactiveCount = row.groups.sum() - currentInactiveCount
        val requiredActiveCount = unknownCount - requiredInactiveCount

        val permutations = permutations("", requiredActiveCount, requiredInactiveCount)

        var rowCount = 0
        for (permutation in permutations) {
            val filledRow = replaceUnknowns(row.springs, permutation)

            if (matches(filledRow, row.groups)) rowCount++
        }
        count += rowCount
    }

    println(count)
}