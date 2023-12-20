package input

import java.io.File

class Input {

    companion object {

        fun read(path: String): List<String> {
            return File(path).readLines()
        }
    }
}