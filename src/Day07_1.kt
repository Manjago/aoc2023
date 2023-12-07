enum class HandType(val strength: String) {
    // Five of a kind, where all five cards have the same label: AAAAA
    FIVE("6"),

    // Four of a kind, where four cards have the same label and one card has a different label: AA8AA
    FOUR("5"),

    // Full house, where three cards have the same label, and the remaining two cards share a different label: 23332
    HOUSE("4"),

    // Three of a kind, where three cards have the same label, and the remaining two cards are each different from any other card in the hand: TTT98
    THREE("3"),

    // Two pair, where two cards share one label, two other cards share a second label, and the remaining card has a third label: 23432
    TWO_PAIR("2"),

    // One pair, where two cards share one label, and the other three cards have a different label from the pair and each other: A23A4
    ONE_PAIR("1"),

    // High card, where all cards' labels are distinct: 23456
    HIGH("0")
}

enum class CardType(val strength: String, val char: Char) {
    A("C", 'A'), K("B", 'K'), Q("A", 'Q'), J("9", 'J'),
    T("8", 'T'), C9("7", '9'), C8("6", '8'), C7("5", '7'),
    C6("4", '6'), C5("3", '5'), C4("2", '4'), C3("1", '3'),
    C2("0", '2');

    companion object {
        private val VALUES = entries.toTypedArray()
        fun fromChar(char: Char): CardType = VALUES.find { it.char == char }!!
    }
}

fun main() {

    data class Hand(val content: List<CardType>, val bid: Int, val handType: HandType, val strength: Long)

    fun parseContent(line: String): List<CardType> = line.split(" ")[0].toCharArray().asSequence()
        .map { CardType.fromChar(it) }.toList()

    fun parseBid(line: String): Int = line.split(" ")[1].toInt()

    fun recode(content: List<CardType>): Map<CardType, Int> {
        val res = mutableMapOf<CardType, Int>()
       for(cardType in content) {
           res.putIfAbsent(cardType, 0)
           res[cardType] = res[cardType]!! + 1
       }
       return res.toMap()
    }

    fun createHand(content: List<CardType>, bid: Int): Hand {

        val distr = recode(content)

        val handType = when {
            distr.keys.size == 1 -> HandType.FIVE
            distr.keys.size == 2 && distr.values.any { it == 4 } -> HandType.FOUR
            distr.keys.size == 2 -> HandType.HOUSE
            distr.keys.size == 3 && distr.values.any { it == 3 } -> HandType.THREE
            distr.keys.size == 3 -> HandType.TWO_PAIR
            distr.keys.size == 4 -> HandType.ONE_PAIR
            else -> HandType.HIGH
        }

        val sb = StringBuilder()
        sb.append(handType.strength)
        for(cardType in content) {
            sb.append(cardType.strength)
        }

        return Hand(content, bid, handType, sb.toString().toLong(16))
    }

    fun part1(input: List<String>): Long {
        val hands = input.asSequence().map {
            createHand(parseContent(it), parseBid(it))
        }.toList()


        val baseForResult = hands.sortedBy { it.strength }

        var result = 0L
        for((index, item) in baseForResult.withIndex()) {
            val rank = index + 1
            //println("$item rank $rank")
            result += item.bid * rank
        }

        return result
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day07_test")
    val part1Test = part1(testInput)
    println("part1Test = $part1Test")
    check(part1Test == 6440L)

    val testInput2 = readInput("Day07_test")
    val part2Test = part2(testInput2)
    println("part2Test = $part2Test")
    check(part2Test == 5)

    val input = readInput("Day07")
    println("part1 = ${part1(input)}")
    println("part2 = ${part2(input)}")
}
