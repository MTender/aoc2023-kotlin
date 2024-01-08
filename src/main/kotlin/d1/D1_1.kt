package d1

import input.Input

fun main() {
    val lines = Input.read("input.txt")

    var sum = 0
    for (line in lines) {
        val firstDigit = line.find { it.isDigit() }!!.digitToInt()
        val lastDigit = line.findLast { it.isDigit() }!!.digitToInt()
        sum += firstDigit * 10 + lastDigit
    }

    println(sum)
}