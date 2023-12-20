package d5

import input.Input

fun findDestination(source: Long, mappings: List<Mapping>): Long {

    for (mapping in mappings) {
        if (mapping.source <= source && source < mapping.source + mapping.length) {
            val offset = mapping.dest - mapping.source
            return source + offset
        }
    }

    return source
}

fun main() {
    val lines = Input.read("input.txt")
    val seeds = parseSeeds(lines[0])
    val mappingLists = getMappingLists(lines)

    val locations = mutableListOf<Long>()

    for (seed in seeds) {
        var value = seed
        for (mappings in mappingLists) {
            value = findDestination(value, mappings)
        }
        locations.add(value)
    }

    println(locations.min())
}

