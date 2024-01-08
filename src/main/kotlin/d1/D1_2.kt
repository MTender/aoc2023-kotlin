package d1

import input.Input

val DIGIT_STRINGS = listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

fun main() {
    val lines = Input.read("input.txt")

    var sum = 0
    for (line in lines) {
        val firstIndexOfDigit = line.indexOfFirst { it.isDigit() }
        val firstIndexOfString = line.indexOfAny(DIGIT_STRINGS)

        val firstDigit = if (firstIndexOfString == -1 || firstIndexOfDigit < firstIndexOfString) {
            line[firstIndexOfDigit].digitToInt()
        } else {
            line.substring(firstIndexOfString).indexOfStartsWith(DIGIT_STRINGS)
        }

        val lastIndexOfDigit = line.indexOfLast { it.isDigit() }
        val lastIndexOfString = line.lastIndexOfAny(DIGIT_STRINGS)

        val lastDigit = if (lastIndexOfString == -1 || lastIndexOfDigit > lastIndexOfString) {
            line[lastIndexOfDigit].digitToInt()
        } else {
            line.substring(lastIndexOfString).indexOfStartsWith(DIGIT_STRINGS)
        }

        sum += firstDigit * 10 + lastDigit
    }

    println(sum)
}

fun String.indexOfStartsWith(strings: List<String>): Int {
    for (i in strings.indices) {
        if (this.startsWith(strings[i])) return i
    }
    return -1
}