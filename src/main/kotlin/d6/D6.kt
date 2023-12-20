package d6

data class Race(
    val time: Int,
    val record: Long
)

fun <T> List<T>.subList(fromIndex: Int): List<T> {
    return this.subList(fromIndex, this.size)
}


fun getRecordOptionsCount(race: Race): Long {
    val halfTime = race.time / 2L

    for (holdTime in 1..halfTime) {
        if (holdTime * (race.time - holdTime) > race.record) {
            return race.time - holdTime * 2 + 1
        }
    }

    throw RuntimeException("did not find any options")
}