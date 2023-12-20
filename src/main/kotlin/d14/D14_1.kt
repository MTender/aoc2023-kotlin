package d14

import input.Input

fun main() {
    val lines = Input.read("input.txt")
    val platform = parseInput(lines)

    rotateRight(platform)
    tiltWest(platform)

    var sum = 0
    for (row in platform) {
        for (i in row.indices) {
            if (row[i] == 'O') sum += row.size - i
        }
    }
    println(sum)
}