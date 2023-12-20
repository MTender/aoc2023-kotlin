package d8

fun parseNetwork(lines: List<String>): Map<String, Pair<String, String>> {
    return lines.associate { line ->
        val fromTo = line.split(" = ")
        val from = fromTo[0]
        val toStr = fromTo[1]
        val leftRight = toStr.substring(1, toStr.length - 1).split(", ")
        val to = Pair(leftRight[0], leftRight[1])
        Pair(from, to)
    }
}