package d22

import input.Input

fun main() {
    val lines = Input.read("input.txt")
    val blocks = parseBlocks(lines).sortedByDescending { it.start.third }
    calculateSupports(blocks)

    val blockMap = blocks.associateBy { it.id }

    var sum = 0
    for (block in blocks) {
        val disintegrated = mutableSetOf<Int>()
        val disintegrationQueue = ArrayDeque<Block>()
        disintegrationQueue.add(block)

        while (disintegrationQueue.isNotEmpty()) {
            val nextBlock = disintegrationQueue.removeFirst()
            disintegrated.add(nextBlock.id)

            for (supportedId in nextBlock.supporting) {
                val supportedBlock = blockMap[supportedId]!!

                val supportedByCopy = supportedBlock.supportedBy.toMutableSet()
                supportedByCopy.removeAll(disintegrated)

                if (supportedByCopy.isEmpty()) {
                    disintegrationQueue.add(supportedBlock)
                }
            }
        }

        sum += disintegrated.size - 1
    }

    println(sum)
}