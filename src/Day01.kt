import kotlin.streams.asSequence

//part2 53363 bad

fun main() {
    fun part1(input: List<String>): Int {
        val sum: Int = input.asSequence().map { string ->
            string.toCharArray().asSequence().map {
                    it.toDigitOtNull()
                }.filterNotNull().toList()
        }.filter {
            it.isNotEmpty()
        }.map { intList ->
            val first = intList.first()
            val last = intList.last()
            first * 10 + last
        }.sum()
        return sum
    }

    fun part2(input: List<String>): Int {
        return input.asSequence().map {
            val toDigits = it.toDigits()
            //println("$it -> $toDigits")
            toDigits
        }.filter { it.isNotEmpty() }
            .map { intList ->
                val first = intList.first()
                val last = intList.last()
                first * 10 + last
            }.sum()

    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    val part1 = part1(testInput)
    println("part1 = $part1")
    check(part1 == 142)
    val testInput2 = readInput("Day01_test2")
    val part2 = part2(testInput2)
    println("part2 = $part2")
    check(part2 == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

fun Char.toDigitOtNull(): Int? = if (this.isDigit()) {
    this - '0'
} else {
    null
}

