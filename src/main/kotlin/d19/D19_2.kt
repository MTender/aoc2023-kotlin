package d19

import d13.split
import input.Input
import kotlin.math.max
import kotlin.math.min

data class Rule(
    val breakpoint: Int,
    val category: Char,
    val operation: Char,
    val target: String
)

fun parseWorkflows2(lines: List<String>): Map<String, List<Rule>> {
    return lines.map { line ->
        val parts = line.split("{", "}")
        val name = parts[0]
        val workflow = parts[1]

        val rules: List<Rule> = workflow.split(",").map rules@{ ruleStr ->
            val conditionAndTarget = ruleStr.split(":")

            if (conditionAndTarget.size == 1) {
                val target = conditionAndTarget[0]

                return@rules Rule(0, 'X', 'X', target)
            }

            val conditionStr = conditionAndTarget[0]
            val target = conditionAndTarget[1]

            return@rules Rule(
                conditionStr.substring(2).toInt(),
                conditionStr[0],
                conditionStr[1],
                target
            )
        }

        Pair(name, rules)
    }.toMap()
}

fun main() {
    val lines = Input.read("input.txt")
    val workflowsAndParts = lines.split { it.isEmpty() }

    val workflows = parseWorkflows2(workflowsAndParts[0])

    val initialRanges = mapOf(
        Pair('x', Pair(1, 4000)),
        Pair('m', Pair(1, 4000)),
        Pair('a', Pair(1, 4000)),
        Pair('s', Pair(1, 4000))
    )

    val combinations = getCombinations(initialRanges, "in", workflows)

    println(combinations)
}

fun getTrueRange(currentRange: Pair<Int, Int>, rule: Rule): Pair<Int, Int>? {
    val range = when (rule.operation) {
        '<' -> {
            val upperBound = min(currentRange.second, rule.breakpoint - 1)
            Pair(currentRange.first, upperBound)
        }

        '>' -> {
            val lowerBound = max(currentRange.first, rule.breakpoint + 1)
            Pair(lowerBound, currentRange.second)
        }

        else -> throw RuntimeException("no such operation")
    }

    return if (range.first <= range.second) range else null
}

fun getFalseRange(currentRange: Pair<Int, Int>, rule: Rule): Pair<Int, Int>? {
    val range = when (rule.operation) {
        '<' -> {
            val lowerBound = max(currentRange.first, rule.breakpoint)
            Pair(lowerBound, currentRange.second)
        }

        '>' -> {
            val upperBound = min(currentRange.second, rule.breakpoint)
            Pair(currentRange.first, upperBound)
        }

        else -> throw RuntimeException("no such operation")
    }

    return if (range.first <= range.second) range else null
}

fun getCategoryRangesCopy(
    categoryRanges: Map<Char, Pair<Int, Int>>,
    category: Char,
    newRange: Pair<Int, Int>
): Map<Char, Pair<Int, Int>> {
    val categoryRangesCopy = categoryRanges.toMutableMap()
    categoryRangesCopy[category] = newRange
    return categoryRangesCopy
}

fun getCombinations(
    categoryRanges: Map<Char, Pair<Int, Int>>,
    workflowLabel: String,
    workflows: Map<String, List<Rule>>
): Long {
    if (workflowLabel == "R") return 0
    if (workflowLabel == "A") return combinationsOfRanges(categoryRanges.values)

    val currentCategoryRanges = categoryRanges.toMutableMap()
    val workflow = workflows[workflowLabel]!!

    var sum = 0L

    for (rule in workflow) {
        if (rule.operation == 'X') return sum + getCombinations(currentCategoryRanges, rule.target, workflows)

        val trueRange = getTrueRange(currentCategoryRanges[rule.category]!!, rule)
        val falseRange = getFalseRange(currentCategoryRanges[rule.category]!!, rule)

        if (trueRange != null) {
            sum += getCombinations(
                getCategoryRangesCopy(currentCategoryRanges, rule.category, trueRange),
                rule.target,
                workflows
            )
        }

        if (falseRange == null) break

        currentCategoryRanges[rule.category] = falseRange
    }

    return sum
}

fun combinationsOfRanges(ranges: Collection<Pair<Int, Int>>): Long {
    return ranges.map { it.second.toLong() - it.first + 1 }.reduce { acc, l -> acc * l }
}