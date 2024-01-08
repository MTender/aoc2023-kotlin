package d2

data class Game(
    val id: Int,
    val sets: List<Triple<Int, Int, Int>>
)

fun parseGame(line: String): Game {
    val idAndSets = line.split(": ")
    val id = idAndSets[0].substringAfterLast(' ').toInt()
    val setStrings = idAndSets[1].split("; ")
    val sets = setStrings.map { setString ->
        val parts = setString.split(", ")

        val red = parts.firstOrNull { it.endsWith("red") }
            ?.substringBefore(' ')?.toInt() ?: 0
        val green = parts.firstOrNull { it.endsWith("green") }
            ?.substringBefore(' ')?.toInt() ?: 0
        val blue = parts.firstOrNull { it.endsWith("blue") }
            ?.substringBefore(' ')?.toInt() ?: 0

        Triple(red, green, blue)
    }

    return Game(id, sets)
}