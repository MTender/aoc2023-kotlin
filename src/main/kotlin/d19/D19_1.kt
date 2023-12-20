package d19

import d13.split
import input.Input

data class Part(
    val x: Int,
    val m: Int,
    val a: Int,
    val s: Int
) {
    constructor(values: List<Int>) : this(values[0], values[1], values[2], values[3])

    fun getTotal(): Int {
        return x + m + a + s
    }
}

fun parseWorkflows(lines: List<String>): Map<String, List<Pair<(Part) -> Boolean, String>>> {
    return lines.map { line ->
        val parts = line.split("{", "}")
        val name = parts[0]
        val workflow = parts[1]

        val rules: List<Pair<(Part) -> Boolean, String>> = workflow.split(",").map rules@{ ruleStr ->
            val conditionAndTarget = ruleStr.split(":")

            if (conditionAndTarget.size == 1) {
                val target = conditionAndTarget[0]
                val condition = { _: Part -> true }

                return@rules Pair(condition, target)
            }

            val conditionStr = conditionAndTarget[0]
            val target = conditionAndTarget[1]

            val condition = when (conditionStr[1]) {
                '>' -> {
                    when (conditionStr[0]) {
                        'x' -> { part: Part -> part.x > conditionStr.substring(2).toInt() }
                        'm' -> { part: Part -> part.m > conditionStr.substring(2).toInt() }
                        'a' -> { part: Part -> part.a > conditionStr.substring(2).toInt() }
                        's' -> { part: Part -> part.s > conditionStr.substring(2).toInt() }
                        else -> throw RuntimeException("unknown field")
                    }
                }

                '<' -> {
                    when (conditionStr[0]) {
                        'x' -> { part: Part -> part.x < conditionStr.substring(2).toInt() }
                        'm' -> { part: Part -> part.m < conditionStr.substring(2).toInt() }
                        'a' -> { part: Part -> part.a < conditionStr.substring(2).toInt() }
                        's' -> { part: Part -> part.s < conditionStr.substring(2).toInt() }
                        else -> throw RuntimeException("unknown field")
                    }
                }

                else -> throw RuntimeException("unknown operation")
            }

            return@rules Pair(condition, target)
        }

        Pair(name, rules)
    }.toMap()
}

fun parseParts(lines: List<String>): List<Part> {
    return lines
        .map {
            it.replace("[\\p{Alpha}{}=]".toRegex(), "")
        }
        .map { line ->
            Part(line.split(",").map { it.toInt() })
        }
}

fun applyWorkflow(
    part: Part,
    workflowLabel: String,
    workflows: Map<String, List<Pair<(Part) -> Boolean, String>>>
): Int {
    val workflow = workflows[workflowLabel]!!

    for (rule in workflow) {
        if (rule.first.invoke(part)) {
            return when (rule.second) {
                "A" -> part.getTotal()
                "R" -> 0
                else -> applyWorkflow(part, rule.second, workflows)
            }
        }
    }

    throw RuntimeException("all rules were rejected")
}

fun main() {
    val lines = Input.read("input.txt")
    val workflowsAndParts = lines.split { it.isEmpty() }

    val workflows = parseWorkflows(workflowsAndParts[0])
    val parts = parseParts(workflowsAndParts[1])

    var sum = 0
    for (part in parts) {
        val partSum = applyWorkflow(part, "in", workflows)
        sum += partSum
    }
    println(sum)
}

