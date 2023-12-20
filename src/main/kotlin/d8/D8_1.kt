package d8

import input.Input

fun main() {
    val lines = Input.read("input.txt")
    val directions = lines[0]

    val network = parseNetwork(lines.subList(2, lines.size))

    var loc = "AAA"
    var count = 0
    while (loc != "ZZZ") {
        val dir = directions[count % directions.length]

        val nodeValue = network[loc]!!

        loc = if (dir == 'L') nodeValue.first else nodeValue.second

        count++
    }

    println(count)
}