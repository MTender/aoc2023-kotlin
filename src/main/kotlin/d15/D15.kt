package d15

fun hash(s: String): Int {
    var value = 0
    for (c in s) {
        value += c.code
        value *= 17
        value %= 256
    }
    return value
}