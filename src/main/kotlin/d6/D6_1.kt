package d6

import input.Input

fun parseRaces(lines: List<String>): List<Race> {
    val times = lines[0].split("\\s+".toRegex()).subList(1).map { it.toInt() }
    val records = lines[1].split("\\s+".toRegex()).subList(1).map { it.toLong() }

    return times.indices.map { Race(times[it], records[it]) }
}

fun main() {
    val lines = Input.read("input.txt")

    val races = parseRaces(lines)

    println(races
        .map { getRecordOptionsCount(it) }
        .reduce { p, e -> p * e }
    )
}