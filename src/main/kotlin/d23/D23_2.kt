package d23

import input.Input

class D23_2 {

    companion object {

        private fun removeAllSlopes(lines: List<String>): List<String> {
            return lines.map { it.replace("[<>v^]".toRegex(), ".") }
        }

        private fun findIntersections(lines: List<String>): List<Pair<Int, Int>> {
            val intersections = mutableListOf<Pair<Int, Int>>()

            for (i in 1..<lines.lastIndex) {
                for (j in 1..<lines[i].lastIndex) {
                    val loc = Pair(i, j)
                    if (isIntersection(lines, loc)) intersections.add(loc)
                }
            }

            return intersections
        }

        private fun isIntersection(lines: List<String>, loc: Pair<Int, Int>): Boolean {
            if (lines[loc] != '.') return false

            val surrounding = getSurrounding(loc)

            return surrounding.count { lines[it] == '.' } > 2
        }

        private fun getIntersectionMoves(lines: List<String>, loc: Pair<Int, Int>): List<Move> {
            val surrounding = getSurrounding(loc)

            return surrounding
                .filter { lines[it] == '.' }
                .map { Move(loc, it, 1) }
        }

        private fun getNextMove(lines: List<String>, move: Move): Move {
            val surrounding = getSurrounding(move.to)

            surrounding.forEach { to ->
                if (lines[to] == '.' && to != move.from) return Move(move.to, to, move.steps + 1)
            }

            throw RuntimeException("No moves possible")
        }

        private fun findNextIntersectionAndSteps(
            lines: List<String>,
            intersections: List<Pair<Int, Int>>,
            move: Move
        ): Pair<Int, Int> {
            val index = intersections.indexOf(move.to)

            if (move.to.first == 0) return Pair(-2, move.steps)
            if (index != -1 || move.to.first == lines.lastIndex) {
                return Pair(index, move.steps)
            }

            return findNextIntersectionAndSteps(lines, intersections, getNextMove(lines, move))
        }

        private data class Position(
            val intersectionIndex: Int,
            val totalSteps: Int,
            val visited: Set<Int>
        )

        fun solve() {
            val lines = removeAllSlopes(Input.read("input.txt"))
            val intersections = findIntersections(lines)

            // intersection to all directly connected intersections with step count
            val directDistance = mutableMapOf<Int, MutableSet<Pair<Int, Int>>>()

            for (fromIntersectionIndex in intersections.indices) {
                val fromIntersection = intersections[fromIntersectionIndex]
                val moves = getIntersectionMoves(lines, fromIntersection)

                directDistance.putIfAbsent(fromIntersectionIndex, mutableSetOf())

                for (move in moves) {
                    val next = findNextIntersectionAndSteps(lines, intersections, move)
                    val toIntersectionIndex = next.first
                    val steps = next.second

                    directDistance[fromIntersectionIndex]!!.add(Pair(toIntersectionIndex, steps))

                    directDistance.putIfAbsent(toIntersectionIndex, mutableSetOf())
                    directDistance[toIntersectionIndex]!!.add(Pair(fromIntersectionIndex, steps))
                }
            }

            val lastIntersectionIndexWithStepsToFinish = directDistance[-1]!!.first()
            val lastIntersectionIndex = lastIntersectionIndexWithStepsToFinish.first
            val stepsFromLastIntersectionToFinish = lastIntersectionIndexWithStepsToFinish.second

            var longestDistance = Int.MIN_VALUE

            val posStack = ArrayDeque<Position>()
            posStack.add(Position(-2, 0, setOf()))

            while (posStack.isNotEmpty()) {
                val pos = posStack.removeLast()

                val nextIntersections = if (pos.intersectionIndex == lastIntersectionIndex) {
                    // do not block last intersection
                    // must turn towards finish
                    listOf(Pair(-1, stepsFromLastIntersectionToFinish))
                } else {
                    directDistance[pos.intersectionIndex]!!
                        .filter {
                            !pos.visited.contains(it.first)
                        }
                }

                nextIntersections.forEach {
                    val to = it.first
                    val totalSteps = pos.totalSteps + it.second

                    if (to != -1) {
                        posStack.add(Position(to, totalSteps, pos.visited + setOf(pos.intersectionIndex)))
                    } else if (totalSteps > longestDistance) {
                        longestDistance = totalSteps
                    }
                }
            }

            println(longestDistance)
        }

    }
}

fun main() {
    D23_2.solve()
}