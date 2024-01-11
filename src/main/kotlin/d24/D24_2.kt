package d24

import input.Input
import java.math.BigDecimal

fun convertFrameOfReference(reference: Hailstone, hailstone: Hailstone): Hailstone {
    return Hailstone(
        hailstone.pos.subtract(reference.pos),
        hailstone.velocity.subtract(reference.velocity)
    )
}

fun findInterceptAndTime(hailstone: Hailstone, normal: Vector): Pair<Vector, BigDecimal> {
    // scalar form of plane: normal.x*x + normal.y*y + normal.z*z == 0
    // parametric form of line: x = pos.x + v.x*t, etc.
    // solve for t

    val t = -(
            normal.x * hailstone.pos.x +
                    normal.y * hailstone.pos.y +
                    normal.z * hailstone.pos.z
            ).divide(
            normal.x * hailstone.velocity.x +
                    normal.y * hailstone.velocity.y +
                    normal.z * hailstone.velocity.z
        )

    // substitute t in parametric form of line
    val intercept = Vector(
        hailstone.pos.x + hailstone.velocity.x * t,
        hailstone.pos.y + hailstone.velocity.y * t,
        hailstone.pos.z + hailstone.velocity.z * t,
    )

    return Pair(intercept, t)
}

fun main() {
    val lines = Input.read("input.txt")
    val hailstones = lines.map { parseHailstone(it) }

    // use the first hailstone's frame of reference
    // this means that its position is (0,0,0) and velocity is (0,0,0)

    val referenceHailstone = hailstones[0]
    val convertedHailstones = hailstones.map { convertFrameOfReference(referenceHailstone, it) }

    // find the normal of the plane created by the first and second hailstone
    // the plane is uniquely identified by normal since the plane contains the point (0,0,0)

    // normal = (p1-p0) X (p2-p0)
    // since p0 = (0,0,0) then
    // normal = p1 X p2

    val secondHailstone = convertedHailstones[1]

    val p1 = secondHailstone.pos
    val p2 = secondHailstone.pos.add(secondHailstone.velocity)

    val normal = p1.crossProduct(p2)

    // find the where and when the third and fourth hailstones intercept the plane

    val thirdHailstone = convertedHailstones[2]
    val fourthHailstone = convertedHailstones[3]
    val (thirdIntercept, thirdInterceptTime) = findInterceptAndTime(thirdHailstone, normal)
    val (fourthIntercept, fourthInterceptTime) = findInterceptAndTime(fourthHailstone, normal)

    // find stone vector
    // distance between intercepts divided by time between intercepts

    val distance = if (thirdInterceptTime < fourthInterceptTime) {
        fourthIntercept.subtract(thirdIntercept)
    } else {
        thirdIntercept.subtract(fourthIntercept)
    }
    val timeDiff = (thirdInterceptTime - fourthInterceptTime).abs()

    val stone = distance.divideEach(timeDiff)

    // find initial position

    val convertedInitial = thirdIntercept.subtract(stone, thirdInterceptTime.toBigInteger())

    // unconvert initial position

    val initial = convertedInitial.add(referenceHailstone.pos)

    println(initial.x + initial.y + initial.z)
}