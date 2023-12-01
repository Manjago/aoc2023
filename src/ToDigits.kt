import java.lang.StringBuilder

fun main() {
    println("7pqrstsixteen".toDigits())
}

fun String.toDigits(): List<Int> {

    val result = mutableListOf<Int>()

    var index = 0;
    val chars = this.toCharArray()
    while(index < chars.size) {
        val c = chars[index]
        if (c.isDigit()) {
            result.add(c - '0')
        } else {
            val stringIntEntry = dict.asSequence().find { this.substring(index).startsWith(it.key) }
            if (stringIntEntry != null) {
                result.add(stringIntEntry.value)
                //index += stringIntEntry.key.length - 1
            }
        }
        index++;
    }

    return result.toList()
}

private val dict = mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9
)