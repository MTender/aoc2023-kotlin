package d24

import input.Input
import java.math.BigDecimal
import java.math.RoundingMode

val MIN = BigDecimal(200_000_000_000_000L)
val MAX = BigDecimal(400_000_000_000_000L)

data class Line(
    val a: BigDecimal,
    val b: BigDecimal
)

fun parseToLine(hailstone: Hailstone): Line {
    val vx = hailstone.velocity.x
    val vy = hailstone.velocity.y

    return Line(
        vy.divide(vx, 100, RoundingMode.HALF_UP),
        hailstone.pos.y - vy * hailstone.pos.x / vx
    )
}

fun calculateIntersect(lineA: Line, lineB: Line): Vector? {
    if (lineA.a == lineB.a) return null // parallel lines

    val intersectX = (lineA.b - lineB.b).divide(lineB.a - lineA.a, 100, RoundingMode.HALF_UP)

    val intersectY = lineB.a * intersectX + lineB.b

    return Vector(intersectX, intersectY, BigDecimal.ZERO)
}

fun checkInBounds(intersect: Vector): Boolean {
    return intersect.x in MIN..MAX && intersect.y in MIN..MAX
}

fun checkInFuture(intersect: Vector, hailstoneA: Hailstone, hailstoneB: Hailstone): Boolean {
    return intersect.x - hailstoneA.pos.x < BigDecimal.ZERO == hailstoneA.velocity.x < BigDecimal.ZERO &&
            intersect.x - hailstoneB.pos.x < BigDecimal.ZERO == hailstoneB.velocity.x < BigDecimal.ZERO
}

fun main() {
    val lines = Input.read("input.txt")
    val hailstones = lines.map { parseHailstone(it) }

    var count = 0
    for (i in hailstones.indices) {
        for (j in (i + 1)..hailstones.lastIndex) {
            val hailstoneA = hailstones[i]
            val hailstoneB = hailstones[j]
            val lineA = parseToLine(hailstoneA)
            val lineB = parseToLine(hailstoneB)

            val intersect = calculateIntersect(lineA, lineB) ?: continue

            val inBounds = checkInBounds(intersect)
            if (!inBounds) continue

            val inFuture = checkInFuture(intersect, hailstoneA, hailstoneB)
            if (!inFuture) continue

            count++
        }
    }

    println(count)
}