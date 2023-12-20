package d16

enum class Direction {
    LEFT,
    RIGHT,
    BELOW,
    ABOVE
}

data class Tile(
    val content: Char,
    var energized: Boolean = false,
    val incomingLightDirections: MutableList<Direction> = mutableListOf()
)

fun move(contraption: List<List<Tile>>, row: Int, col: Int, from: Direction) {
    if (
        row < 0 ||
        row >= contraption.size ||
        col < 0 ||
        col >= contraption[0].size
    ) return

    val tile = contraption[row][col]
    tile.energized = true

    if (tile.incomingLightDirections.contains(from)) return

    tile.incomingLightDirections.add(from)

    when (tile.content) {
        '.' -> {
            when (from) {
                Direction.LEFT -> move(contraption, row, col + 1, from)
                Direction.RIGHT -> move(contraption, row, col - 1, from)
                Direction.BELOW -> move(contraption, row - 1, col, from)
                Direction.ABOVE -> move(contraption, row + 1, col, from)
            }
        }

        '/' -> {
            when (from) {
                Direction.LEFT -> move(contraption, row - 1, col, Direction.BELOW)
                Direction.RIGHT -> move(contraption, row + 1, col, Direction.ABOVE)
                Direction.BELOW -> move(contraption, row, col + 1, Direction.LEFT)
                Direction.ABOVE -> move(contraption, row, col - 1, Direction.RIGHT)
            }
        }

        '\\' -> {
            when (from) {
                Direction.LEFT -> move(contraption, row + 1, col, Direction.ABOVE)
                Direction.RIGHT -> move(contraption, row - 1, col, Direction.BELOW)
                Direction.BELOW -> move(contraption, row, col - 1, Direction.RIGHT)
                Direction.ABOVE -> move(contraption, row, col + 1, Direction.LEFT)
            }
        }

        '|' -> {
            when (from) {
                Direction.LEFT, Direction.RIGHT -> {
                    move(contraption, row - 1, col, Direction.BELOW)
                    move(contraption, row + 1, col, Direction.ABOVE)
                }

                Direction.BELOW -> move(contraption, row - 1, col, from)
                Direction.ABOVE -> move(contraption, row + 1, col, from)
            }
        }

        '-' -> {
            when (from) {
                Direction.LEFT -> move(contraption, row, col + 1, from)
                Direction.RIGHT -> move(contraption, row, col - 1, from)
                Direction.BELOW, Direction.ABOVE -> {
                    move(contraption, row, col - 1, Direction.RIGHT)
                    move(contraption, row, col + 1, Direction.LEFT)
                }
            }
        }
    }
}

fun parseInput(lines: List<String>): List<List<Tile>> {
    return lines.map { line -> line.toCharArray().map { Tile(it) } }
}

fun countEnergized(contraption: List<List<Tile>>): Int {
    var sum = 0
    for (line in contraption) {
        for (tile in line) {
            if (tile.energized) sum++
        }
    }
    return sum
}