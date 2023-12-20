package d14

import input.Input
import kotlin.math.sqrt

fun getPlatformString(platform: Array<CharArray>): String {
    return platform.joinToString("") { it.joinToString("") }
}

fun parsePlatformStr(platformStr: String): Array<CharArray> {
    val lineLength = sqrt(platformStr.length.toDouble()).toInt()
    val lines = (0..<lineLength).map { platformStr.substring(it * lineLength, (it + 1) * lineLength) }
    return parseInput(lines)
}

fun spinPlatform(platform: Array<CharArray>) {
    for (i in 0..3) {
        tiltWest(platform)
        rotateLeft(platform)
    }
}

fun main() {
    val lines = Input.read("input.txt")
    val platform = parseInput(lines)

    // to start with north
    rotateRight(platform)

    val positions = hashMapOf<String, Int>()
    positions[getPlatformString(platform)] = 0

    var index = 1
    while (true) {
        spinPlatform(platform)
        val platformStr = getPlatformString(platform)

        if (positions.contains(platformStr)) break

        positions[platformStr] = index++
    }

    val cycleStartIndex = positions[getPlatformString(platform)]!!

    val endPosOffset = (1_000_000_000 - cycleStartIndex) % (index - cycleStartIndex)

    val endPlatformStr = positions.entries.find { it.value == cycleStartIndex + endPosOffset }!!.key

    val endPlatform = parsePlatformStr(endPlatformStr)

    var sum = 0
    for (row in endPlatform) {
        for (i in row.indices) {
            if (row[i] == 'O') sum += row.size - i
        }
    }
    println(sum)
}

