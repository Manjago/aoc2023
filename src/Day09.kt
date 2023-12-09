fun main() {

    fun parse(source: String): List<Long> = source.split(" ").asSequence().map {
        it.trim().toLong()
    }.toList()

    fun parse(input: List<String>): Sequence<List<Long>> = input.asSequence().map { parse(it) }

    fun List<Long>.allZeros() = this.any { it != 0L }.not()

    fun List<Long>.makeDiff(): List<Long> {
        val result = mutableListOf<Long>()

        if (this.size == 1) {
            throw IllegalArgumentException()
        }

        for (i in 0 until (this.size - 1)) {
            result.add(this[i + 1] - this[i])
        }

        return result.toList()
    }

    fun fillStack(
        data: List<Long>, stack: ArrayDeque<List<Long>>
    ) {
        var current = data
        stack += current
        do {
            current = current.makeDiff()
            stack += current
        } while (!current.allZeros())
    }

    fun predict(data: List<Long>): Long {

        val stack = ArrayDeque<List<Long>>()
        fillStack(data, stack)

        var current = stack.removeLast()

        // now all zeros on stack top
        check(current.isNotEmpty() && current.allZeros())
        var addition = 0L

        while (stack.isNotEmpty()) {
            current = stack.removeLast()
            addition += current.last()
        }
        return addition
    }

    fun part1(input: List<String>): Long = parse(input).map {
        predict(it)
    }.sum()

    val testInput = readInput("Day09_test")
    val part1Test = part1(testInput)
    println("part1Test = $part1Test")
    check(part1Test == 114L)

    val input = readInput("Day09")
    println("part1 = ${part1(input)}")
}
