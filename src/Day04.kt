fun main() {

    data class Card(val id: Int, val winners: Set<Int>, val ours: List<Int>)

    fun parse(line: String): Card {
        // Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        val id = line.substringAfter("Card ").substringBefore(":").trim().toInt()
        val winners = line.substringAfter(": ").substringBefore(" |").split(" ").asSequence().filter { it.isNotBlank() }
            .map { it.toInt() }.toSet()
        val ours =
            line.substringAfter("| ").trim().split(" ").asSequence().filter { it.isNotBlank() }.map { it.toInt() }
                .toList()
        return Card(id, winners, ours)
    }

    fun power2(value: Int): Int = if (value == 0) {
        1
    } else {
        2 * power2(value - 1)
    }

    fun calcPoint(card: Card): Int {
        val sum = card.ours.asSequence().map {
            if (card.winners.contains(it)) 1 else 0
        }.sum()

        return if (sum == 0) {
            0
        } else {
            power2(sum - 1)
        }
    }

    fun part1(input: List<String>): Int = input.asSequence().map {
        parse(it)
    }.map {
        calcPoint(it)
    }.sum()

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day04_test")
    val part1Test = part1(testInput)
    println("part1Test = $part1Test")
    check(part1Test == 13)

    val testInput2 = readInput("Day04_test")
    val part2Test = part2(testInput2)
    println("part2Test = $part2Test")
    check(part2Test == 6)

    val input = readInput("Day04")
    println("part1 = ${part1(input)}")
    println("part2 = ${part2(input)}")
}
