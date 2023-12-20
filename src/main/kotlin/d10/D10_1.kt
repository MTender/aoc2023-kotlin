package d10

import input.Input

fun main() {
    val lines = Input.read("input.txt")

    val diagram = padInput(lines)

    val start = findStart(diagram)

    val firstConnections = findConnections(diagram, start)

    var prev = start.copy()
    var loc = firstConnections[0].copy()
    var count = 1

    while (loc != start) {
        val cons = findConnections(diagram, loc)
        val next = if (cons[0] == prev) cons[1] else cons[0]

        prev = loc.copy()
        loc = next.copy()
        count++
    }

    println(count / 2)
}