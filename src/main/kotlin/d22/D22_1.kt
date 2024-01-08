package d22

import input.Input

fun main() {
    val lines = Input.read("input.txt")
    val blocks = parseBlocks(lines)
    calculateSupports(blocks)

    val blockMap = blocks.associateBy { it.id }

    println(blocks.filter { block ->
        block.supporting.isEmpty() ||
        block.supporting.all { supportedBlockId ->
            blockMap[supportedBlockId]!!.supportedBy.size > 1
        }
    }.size)
}