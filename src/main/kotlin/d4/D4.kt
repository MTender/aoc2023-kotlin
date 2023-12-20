package d4

fun getMatchesCount(line: String): Int {

    val content = line.split(": ")[1]
    val parts = content.split(" \\| ")

    val winningStr = parts[0]
    val haveStr = parts[1]

    val winning = winningStr.split(" ")
    val have = haveStr.split(" ")

    return winning.intersect(have.toSet()).size
}