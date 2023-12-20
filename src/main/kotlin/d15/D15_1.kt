package d15

import input.Input

fun main() {
    val lines = Input.read("input.txt")
    val sequence = lines[0].split(",")

    var sum = 0
    for (step in sequence) {
        val value = hash(step)
        sum += value
    }
    println(sum)
}