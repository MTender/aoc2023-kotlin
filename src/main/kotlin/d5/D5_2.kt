package d5

import input.Input

fun findRangeDestination(sourceRange: Pair<Long, Long>, mappings: List<Mapping>): List<Pair<Long, Long>> {
    val sourceStart = sourceRange.first
    val sourceLength = sourceRange.second

    val mappedRanges = mutableListOf<Pair<Long, Long>>()
    val destinations = mutableListOf<Pair<Long, Long>>()

    for (mapping in mappings) {
        val offset = mapping.dest - mapping.source

        if (mapping.source <= sourceStart && sourceStart < mapping.source + mapping.length) {

            val destStart = sourceStart + offset

            val destLength = if (sourceStart + sourceLength < mapping.source + mapping.length)
                sourceLength
            else mapping.source + mapping.length - sourceStart

            mappedRanges.add(Pair(sourceStart, destLength))
            destinations.add(Pair(destStart, destLength))

        } else if (sourceStart < mapping.source && mapping.source < sourceStart + sourceLength) {

            val destStart = mapping.source + offset

            val destLength = if (sourceStart + sourceLength < mapping.source + mapping.length)
                sourceStart + sourceLength - mapping.source
            else mapping.length

            mappedRanges.add(Pair(mapping.source, destLength))
            destinations.add(Pair(destStart, destLength))
        }
    }

    destinations.addAll(getComplementRanges(sourceRange, mappedRanges))

    return destinations
}

fun getComplementRanges(parentRange: Pair<Long, Long>, ranges: List<Pair<Long, Long>>): List<Pair<Long, Long>> {
    var start = parentRange.first
    val end = parentRange.first + parentRange.second - 1

    val sortedRanges = ranges.sortedBy { it.first }

    val complementRanges = mutableListOf<Pair<Long, Long>>()

    for (range in sortedRanges) {
        if (start < range.first)
            complementRanges.add(Pair(start, range.first - start))
        start = range.first + range.second
    }
    if (start < end)
        complementRanges.add(Pair(start, end - start + 1))

    return complementRanges
}

fun main() {
    val lines = Input.read("input.txt")
    val seeds = parseSeeds(lines[0])
    val mappingLists = getMappingLists(lines)

    var ranges = mutableListOf<Pair<Long, Long>>()
    for (i in seeds.indices step 2) {
        ranges.add(Pair(seeds[i], seeds[i + 1]))
    }

    for (mappings in mappingLists) {

        val newRanges = mutableListOf<Pair<Long, Long>>()
        for (range in ranges) {
            newRanges.addAll(findRangeDestination(range, mappings))
        }
        ranges = newRanges
    }

    val locations = ranges.map { it.first }

    println(locations.min())
}