package d20

data class Pulse(
    val from: String,
    val to: String,
    val high: Boolean
)

interface Module {
    val name: String

    fun handlePulse(pulse: Pulse): List<Pulse>
}

class FlipFlopModule(
    override val name: String,
    private val destinations: List<String>,
) : Module {
    private var enabled = false

    override fun handlePulse(pulse: Pulse): List<Pulse> {
        if (pulse.to != name) throw RuntimeException("wrong target")
        if (pulse.high) return listOf()

        enabled = !enabled
        if (enabled) {
            return destinations.map { Pulse(name, it, true) }
        }
        return destinations.map { Pulse(name, it, false) }
    }
}

class ConjunctionModule(
    override val name: String,
    private val destinations: List<String>,
    inputs: List<String>
) : Module {
    private val lastPulsesHigh = mutableMapOf<String, Boolean>().apply { putAll(inputs.map { Pair(it, false) }) }

    override fun handlePulse(pulse: Pulse): List<Pulse> {
        if (pulse.to != name) throw RuntimeException("wrong target")

        lastPulsesHigh[pulse.from] = pulse.high

        if (lastPulsesHigh.values.all { it }) return destinations.map { Pulse(name, it, false) }
        return destinations.map { Pulse(name, it, true) }
    }
}

class BroadcasterModule(
    override val name: String,
    private val destinations: List<String>,
) : Module {

    override fun handlePulse(pulse: Pulse): List<Pulse> {
        if (pulse.to != name) throw RuntimeException("wrong target")

        return destinations.map { Pulse(name, it, pulse.high) }
    }
}

data class InputDataLine(
    val type: Char,
    val name: String,
    val destinations: List<String>
)

fun parseInput(lines: List<String>): List<InputDataLine> {
    return lines.map { line ->
        val parts = line.split(" -> ")
        val destinations = parts[1].split(", ")

        val name = if (line[0] == 'b') parts[0] else parts[0].substring(1)
        InputDataLine(line[0], name, destinations)
    }
}

fun parseModules(input: List<InputDataLine>): Map<String, Module> {
    fun getInputs(name: String): List<String> {
        return input.filter { it.destinations.contains(name) }.map { it.name }
    }

    return input.associate { line ->
        when (line.type) {
            '%' -> {
                Pair(line.name, FlipFlopModule(line.name, line.destinations))
            }

            '&' -> {
                Pair(line.name, ConjunctionModule(line.name, line.destinations, getInputs(line.name)))
            }

            'b' -> {
                Pair(line.name, BroadcasterModule(line.name, line.destinations))
            }

            else -> throw RuntimeException("unknown module type")
        }
    }
}