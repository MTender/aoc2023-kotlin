package d15

import input.Input

data class Lens(
    val label: String,
    val focalLength: Int
)

fun main() {
    val lines = Input.read("input.txt")
    val sequence = lines[0]
        .split(",")

    val boxes = Array<List<Lens>>(256) { _ -> listOf() }

    for (step in sequence) {
        val parts = step.split("[=-]".toRegex())
        val label = parts[0]
        val boxNr = hash(label)

        if (parts[1].isEmpty()) {
            boxes[boxNr] = boxes[boxNr].filter { it.label != label }
        } else {
            val box = boxes[boxNr]
            val newLens = Lens(label, parts[1].toInt())

            val replace = box.any { it.label == label }

            if (replace) {
                boxes[boxNr] = box.map { if (it.label == label) newLens else it }
            } else {
                boxes[boxNr] += listOf(newLens)
            }
        }
    }

    var sum = 0

    for ((boxNr, box) in boxes.withIndex()) {
        for ((pos, lens) in box.withIndex()) {
            val focusingPower = (1 + boxNr) * (1 + pos) * lens.focalLength
            sum += focusingPower
        }
    }

    println(sum)
}