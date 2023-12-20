package d8

import input.Input

fun main() {
    val lines = Input.read("input.txt")
    val directions = lines[0]

    val network = parseNetwork(lines.subList(2, lines.size))

    val locs = network.keys.filter { it[it.lastIndex] == 'A' }.toMutableList()

    val first = LongArray(6)

    var count = 0
    while (first.any { it == 0L }) {
        val dir = directions[count % directions.length]

        for (i in locs.indices) {
            val loc = locs[i]

            if (loc[loc.lastIndex] == 'Z' && first[i] == 0L) {
                first[i] = count.toLong()
            }

            val nodeValue = network[loc]!!

            locs[i] = if (dir == 'L') nodeValue.first else nodeValue.second
        }

        count++
    }

    println(first
        .reduce { acc, i -> lcm(acc, i) }
    )
}

fun gcd(a: Long, b: Long): Long {
    var num1 = a
    var num2 = b
    while (num2 != 0L) {
        val temp = num2
        num2 = num1 % num2
        num1 = temp
    }
    return num1
}

fun lcm(a: Long, b: Long): Long {
    return a / gcd(a, b) * b
}