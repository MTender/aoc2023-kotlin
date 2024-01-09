package d23

import d16.Direction
import input.Input

class D23_1 {

    companion object {

        private fun findSplittingIntersections(lines: List<String>): List<Pair<Int, Int>> {
            val intersections = mutableListOf<Pair<Int, Int>>()

            for (i in 1..<lines.lastIndex) {
                for (j in 1..<lines[i].lastIndex) {
                    val loc = Pair(i, j)
                    if (isSplittingIntersection(lines, loc)) intersections.add(loc)
                }
            }

            return intersections
        }

        private fun isPassableSlope(lines: List<String>, loc: Pair<Int, Int>, dir: Direction): Boolean {
            return when (dir) {
                Direction.ABOVE -> lines[loc.first - 1][loc.second] == '^'
                Direction.BELOW -> lines[loc.first + 1][loc.second] == 'v'
                Direction.LEFT -> lines[loc.first][loc.second - 1] == '<'
                Direction.RIGHT -> lines[loc.first][loc.second + 1] == '>'
            }
        }

        private fun isSplittingIntersection(lines: List<String>, loc: Pair<Int, Int>): Boolean {
            return Direction.entries.count { isPassableSlope(lines, loc, it) } == 2
        }

        private fun getIntersectionMoves(lines: List<String>, loc: Pair<Int, Int>): List<Move> {
            val moves = mutableListOf<Move>()

            if (isPassableSlope(lines, loc, Direction.ABOVE)) moves.add(Move(loc, Pair(loc.first - 1, loc.second), 1))
            if (isPassableSlope(lines, loc, Direction.BELOW)) moves.add(Move(loc, Pair(loc.first + 1, loc.second), 1))
            if (isPassableSlope(lines, loc, Direction.LEFT)) moves.add(Move(loc, Pair(loc.first, loc.second - 1), 1))
            if (isPassableSlope(lines, loc, Direction.RIGHT)) moves.add(Move(loc, Pair(loc.first, loc.second + 1), 1))

            return moves
        }

        private fun getNextMove(lines: List<String>, move: Move): Move {
            val top = Pair(move.to.first - 1, move.to.second)
            if (lines[top] in listOf('.', '^') && top != move.from) return Move(move.to, top, move.steps + 1)

            val bottom = Pair(move.to.first + 1, move.to.second)
            if (lines[bottom] in listOf('.', 'v') && bottom != move.from) return Move(move.to, bottom, move.steps + 1)

            val left = Pair(move.to.first, move.to.second - 1)
            if (lines[left] in listOf('.', '<') && left != move.from) return Move(move.to, left, move.steps + 1)

            val right = Pair(move.to.first, move.to.second + 1)
            if (lines[right] in listOf('.', '>') && right != move.from) return Move(move.to, right, move.steps + 1)

            throw RuntimeException("No moves possible")
        }

        private fun findNextIntersectionAndSteps(
            lines: List<String>,
            intersections: List<Pair<Int, Int>>,
            move: Move
        ): Pair<Int, Int> {
            val index = intersections.indexOf(move.to)

            if (index != -1 || move.to.first == lines.lastIndex) {
                return Pair(index, move.steps)
            }

            return findNextIntersectionAndSteps(lines, intersections, getNextMove(lines, move))
        }

        fun solve() {
            val lines = Input.read("input.txt")
            val intersections = findSplittingIntersections(lines)
            val start = Pair(0, lines[0].indexOf('.'))

            // directed edge to step count
            val longestDirectDistance = mutableMapOf<Pair<Int, Int>, Int>()

            for (fromIntersectionIndex in intersections.indices) {
                val fromIntersection = intersections[fromIntersectionIndex]
                val moves = getIntersectionMoves(lines, fromIntersection)

                for (move in moves) {
                    val next = findNextIntersectionAndSteps(lines, intersections, move)
                    val toIntersectionIndex = next.first
                    val steps = next.second

                    longestDirectDistance.putIfLargerOrAbsent(
                        Pair(
                            fromIntersectionIndex,
                            toIntersectionIndex
                        ),
                        steps
                    )
                }
            }

            val startToFirstIntersection = findNextIntersectionAndSteps(
                lines,
                intersections,
                Move(start, Pair(start.first + 1, start.second), 1)
            )

            longestDirectDistance[Pair(-2, startToFirstIntersection.first)] = startToFirstIntersection.second

            val finishDistances = mutableListOf<Int>()

            val posStack = ArrayDeque<Pair<Int, Int>>()
            posStack.add(startToFirstIntersection)

            while (posStack.isNotEmpty()) {
                val pos = posStack.removeLast()

                val nextIntersections = longestDirectDistance.filter { it.key.first == pos.first }

                nextIntersections.forEach {
                    val to = it.key.second
                    val totalSteps = pos.second + it.value
                    if (to == -1) finishDistances.add(totalSteps)
                    else posStack.add(Pair(to, totalSteps))
                }
            }

            println(finishDistances.max())
        }

    }
}

fun main() {
    D23_1.solve()
}