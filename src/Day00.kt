fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day00_test")
    val part1Test = part1(testInput)
    println("part1Test = $part1Test")
    check(part1Test == 1)

    val testInput2 = readInput("Day00_test_2")
    val part2Test = part2(testInput2)
    println("part2Test = $part2Test")
    check(part2Test == 1)

    val input = readInput("Day00")
    println("part1 = ${part1(input)}")
    println("part2 = ${part2(input)}")
}
