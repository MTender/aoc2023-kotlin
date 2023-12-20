package d14

fun parseInput(lines: List<String>): Array<CharArray> {
    return lines.map { it.toCharArray() }.toTypedArray()
}

fun rotateRight(platform: Array<CharArray>) {
    val copy = Array(platform.size) { i -> platform[i].clone() }
    for (i in copy.indices) {
        for (j in copy[i].indices) {
            platform[platform.size - j - 1][i] = copy[i][j]
        }
    }
}

fun rotateLeft(platform: Array<CharArray>) {
    val copy = Array(platform.size) { i -> platform[i].clone() }
    for (i in copy.indices) {
        for (j in copy[i].indices) {
            platform[j][platform[0].size - 1 - i] = copy[i][j]
        }
    }
}

fun tiltWest(platform: Array<CharArray>) {
    platform.forEach { bubble(it) }
}

fun swap(arr: CharArray, i1: Int, i2: Int) {
    val temp = arr[i1]
    arr[i1] = arr[i2]
    arr[i2] = temp
}

fun bubble(line: CharArray) {
    for (i in 1..line.lastIndex) {
        var offset = 0
        while (i - 1 - offset >= 0 && line[i - 1 - offset] == '.' && line[i - offset] == 'O') {
            swap(line, i - 1 - offset, i - offset)
            offset++
        }
    }
}