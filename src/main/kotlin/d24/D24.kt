package d24

import java.math.BigDecimal
import java.math.BigInteger

data class Vector(
    val x: BigDecimal,
    val y: BigDecimal,
    val z: BigDecimal
) {
    fun add(other: Vector): Vector {
        return Vector(
            this.x + other.x,
            this.y + other.y,
            this.z + other.z
        )
    }

    fun subtract(other: Vector, count: BigInteger = BigInteger.ONE): Vector {
        return Vector(
            this.x - other.x * count.toBigDecimal(),
            this.y - other.y * count.toBigDecimal(),
            this.z - other.z * count.toBigDecimal()
        )
    }

    fun crossProduct(other: Vector): Vector {
        return Vector(
            this.y * other.z - this.z * other.y,
            -(this.x * other.z - this.z * other.x),
            this.x * other.y - this.y * other.x
        )
    }

    fun divideEach(value: BigDecimal): Vector {
        return Vector(
            this.x.divide(value),
            this.y.divide(value),
            this.z.divide(value)
        )
    }
}

data class Hailstone(
    val pos: Vector,
    val velocity: Vector
)

fun List<Long>.toVector(): Vector {
    if (this.size != 3) throw UnsupportedOperationException("Array size must be 3 to parse to vector")
    return Vector(this[0].toBigDecimal(), this[1].toBigDecimal(), this[2].toBigDecimal())
}

fun parseHailstone(line: String): Hailstone {
    val parts = line.split(" @ ")
    val pos = parts[0].split(", ").map { it.toLong() }.toVector()
    val velocity = parts[1].split(", ").map { it.toLong() }.toVector()
    return Hailstone(pos, velocity)
}