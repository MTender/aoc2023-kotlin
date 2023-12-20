package d20

import d8.lcm
import input.Input

fun main() {
    val lines = Input.read("input.txt")
    val input = parseInput(lines)
    val modules = parseModules(input)

    var countButton = 0L

    val cycles = mutableListOf<Long>()

    outer@ while (true) {
        countButton++

        val pulses = ArrayDeque<Pulse>()
        pulses.add(Pulse("X", "broadcaster", false))

        while (pulses.isNotEmpty()) {
            val nextPulse = pulses.removeFirst()

            if (modules[nextPulse.to] == null) continue
            val target = modules[nextPulse.to]!!

            if (nextPulse.to == "vr" && nextPulse.high) {
                cycles.add(countButton)
            }

            val output = target.handlePulse(nextPulse)

            pulses.addAll(output)
        }

        if (countButton == 4096L) break
    }

    println(cycles.toLongArray()
        .reduce { acc, i -> lcm(acc, i) }
    )
}