package d9

fun parseInput(lines: List<String>): List<List<Int>> {
    return lines.map { line -> line.split(" ").map { it.toInt() } }
}