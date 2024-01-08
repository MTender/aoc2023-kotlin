package d22

import kotlin.math.max

data class Block(
    val id: Int,
    val start: Triple<Int, Int, Int>,
    val end: Triple<Int, Int, Int>,
    val supporting: MutableSet<Int> = mutableSetOf(),
    val supportedBy: MutableSet<Int> = mutableSetOf()
)

data class ViewInfo(
    val blockId: Int?,
    val height: Int
)

fun parseBlocks(lines: List<String>): List<Block> {
    return lines.mapIndexed { index, line ->
        val startAndEndStrings = line.split("~")

        val start = startAndEndStrings[0].split(",").map { it.toInt() }.toTriple()
        val end = startAndEndStrings[1].split(",").map { it.toInt() }.toTriple()

        Block(index, start, end)
    }
}

fun <T> List<T>.toTriple(): Triple<T, T, T> {
    if (this.size != 3) throw UnsupportedOperationException("List must have exactly 3 elements")
    return Triple(this[0], this[1], this[2])
}

fun Triple<Int, Int, Int>.subtract(other: Triple<Int, Int, Int>): Triple<Int, Int, Int> {
    return Triple(
        this.first - other.first,
        this.second - other.second,
        this.third - other.third
    )
}

fun calculateSupports(blocks: List<Block>) {
    val sortedBlocks = blocks.sortedBy { it.start.third }

    val largestX = sortedBlocks.maxOf { max(it.start.first, it.end.first) }
    val largestY = sortedBlocks.maxOf { max(it.start.second, it.end.second) }

    val topDown = Array(largestX + 1) { Array(largestY + 1) { ViewInfo(null, 0) } }


    val blockMap = sortedBlocks.associateBy { it.id }

    for (block in sortedBlocks) {
        val locs = mutableListOf<Pair<Int, Int>>()

        val diff = block.end.subtract(block.start)

        if (diff.toList().any { it < 0 }) throw RuntimeException("what the hell")

        if (diff.first != 0) {
            // x-axis length
            for (i in 0..diff.first) {
                locs.add(Pair(block.start.first + i, block.start.second))
            }
        } else if (diff.second != 0) {
            // y-axis length
            for (i in 0..diff.second) {
                locs.add(Pair(block.start.first, block.start.second + i))
            }
        } else {
            // z-axis length or single cube
            locs.add(Pair(block.start.first, block.start.second))
        }

        val belowLocs = locs.map { topDown[it.first][it.second] }
        val maxHeightBelow = belowLocs.maxOf { it.height }

        val supportedByBlockIds = belowLocs.filter { it.height == maxHeightBelow }.mapNotNull { it.blockId }.toSet()

        supportedByBlockIds.map { id -> blockMap[id]!! }.forEach { supportBlock ->
            supportBlock.supporting.add(block.id)
        }
        block.supportedBy.addAll(supportedByBlockIds)

        locs.forEach { loc ->
            topDown[loc.first][loc.second] = ViewInfo(block.id, maxHeightBelow + 1 + diff.third)
        }
    }
}