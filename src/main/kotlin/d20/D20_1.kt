package d20

import input.Input

fun main() {
    val lines = Input.read("input.txt")
    val input = parseInput(lines)
    val modules = parseModules(input)

    var countLow = 0L
    var countHigh = 0L

    for (i in 1..1000) {
        val pulses = ArrayDeque<Pulse>()
        pulses.add(Pulse("X", "broadcaster", false))

        while (pulses.isNotEmpty()) {
            val nextPulse = pulses.removeFirst()
            if (nextPulse.high) countHigh++ else countLow++

            if (modules[nextPulse.to] == null) continue
            val target = modules[nextPulse.to]!!

            val output = target.handlePulse(nextPulse)

            pulses.addAll(output)
        }
    }

    println(countLow * countHigh)
}