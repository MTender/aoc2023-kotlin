package d12

class D12part2(
    private val memo: MutableMap<Triple<Int, Int, Int>, Long>,
    private val springs: String,
    private val groups: List<Int>
) {

    fun getOptionsCountMemo(
        current: String,
        nextIndex: Int,
        groupIndex: Int,
        groupVal: Int
    ): Long {
        return memo.getOrPut(
            Triple(nextIndex, groupIndex, groupVal)
        ) {
            getOptionsCount(
                current,
                nextIndex,
                groupIndex,
                groupVal
            )
        }
    }

    private fun getOptionsCount(
        current: String,
        nextIndex: Int,
        groupIndex: Int,
        groupVal: Int
    ): Long {
        if (nextIndex == springs.length) {
            if (groupIndex == groups.lastIndex) return 1L
            return 0
        }

        if (groupIndex == groups.lastIndex) {
            if (springs.substring(nextIndex).any { it == '#' }) {
                return 0L
            }
            return 1L
        }

        val c = springs[nextIndex]
        return when (c) {
            '.' -> {
                handleDot(current, nextIndex, groupIndex, groupVal)
            }

            '#' -> {
                handlePound(current, nextIndex, groupIndex, groupVal)
            }

            '?' -> {
                handleDot(current, nextIndex, groupIndex, groupVal) +
                        handlePound(current, nextIndex, groupIndex, groupVal)
            }

            else -> throw RuntimeException("unknown character")
        }
    }

    private fun handleDot(
        current: String,
        nextIndex: Int,
        groupIndex: Int,
        groupVal: Int
    ): Long {
        if (groupVal == 0) {
            // must end current group
            return getOptionsCountMemo(
                "$current.",
                nextIndex + 1,
                groupIndex + 1,
                groups[groupIndex + 1]
            )
        }

        if (groupVal == groups[groupIndex]) {
            // can be no-op
            return getOptionsCountMemo(
                "$current.",
                nextIndex + 1,
                groupIndex,
                groupVal
            )
        }

        return 0
    }

    private fun handlePound(
        current: String,
        nextIndex: Int,
        groupIndex: Int,
        groupVal: Int
    ): Long {
        if (groupVal == 0) return 0

        return getOptionsCountMemo(
            "$current#",
            nextIndex + 1,
            groupIndex,
            groupVal - 1
        )
    }


}