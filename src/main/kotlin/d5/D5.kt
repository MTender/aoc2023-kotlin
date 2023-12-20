package d5

fun parseSeeds(line: String): List<Long> {
    val seedsStr = line.split(": ")[1]

    return seedsStr
        .split(" ")
        .map { it.toLong() }
}

fun splitOnEmptyLines(lines: List<String>): List<List<String>> {
    val result = mutableListOf(mutableListOf<String>())

    for (line in lines) {
        if (line.isEmpty()) {
            result.add(mutableListOf())
        } else {
            result[result.lastIndex].add(line)
        }
    }

    for (list in result) {
        list.removeFirst()
    }

    return result
}

fun parseLine(line: String): Mapping {
    val values = line.split(" ")

    return Mapping(values[0].toLong(), values[1].toLong(), values[2].toLong())
}

fun getMappingLists(lines: List<String>): List<List<Mapping>> {
    val rawMappingLists = splitOnEmptyLines(lines.subList(2, lines.size))
    return rawMappingLists.map { it.map { mappingStr -> parseLine(mappingStr) } }
}

data class Mapping(
    val dest: Long,
    val source: Long,
    val length: Long
)