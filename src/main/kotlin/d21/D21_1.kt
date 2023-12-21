package d21

import input.Input

fun main() {
    val lines = Input.read("input.txt")

    val start = findStart(lines)
    val steps = 11

    val even = (start.first + start.second + steps) % 2 == 0

    val markedLines = bfs(lines, steps, start)

    val count = count(markedLines, even)
    println(count)
}