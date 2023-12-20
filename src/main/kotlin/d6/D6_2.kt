package d6

import input.Input

fun parseRace(lines: List<String>): Race {
    val time = lines[0].split(":\\s+".toRegex())[1].replace(" ", "").toInt()
    val record = lines[1].split(":\\s+".toRegex())[1].replace(" ", "").toLong()

    return Race(time, record)
}

fun main() {
    val lines = Input.read("input.txt")

    val race = parseRace(lines)

    val count = getRecordOptionsCount(race)
    println(count)
}