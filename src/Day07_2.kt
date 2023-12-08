import HandType.FIVE
import HandType.FOUR
import HandType.HIGH
import HandType.HOUSE
import HandType.ONE_PAIR
import HandType.THREE
import HandType.TWO_PAIR

enum class MCardType(val strength: String, val char: Char) {
    A("C", 'A'), K("B", 'K'), Q("A", 'Q'), T("9", 'T'), C9("8", '9'), C8("7", '8'), C7("6", '7'), C6("5", '6'), C5(
        "4",
        '5'
    ),
    C4("3", '4'), C3("2", '3'), C2("1", '2'), J("0", 'J');

    companion object {
        private val VALUES = entries.toTypedArray()
        fun fromChar(char: Char) = VALUES.find { it.char == char }!!
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun main() {

    data class Hand(val content: List<MCardType>, val bid: Int, val handType: HandType, val strength: Long)

    fun parseContent(line: String): List<MCardType> =
        line.split(" ")[0].toCharArray().asSequence().map { MCardType.fromChar(it) }.toList()

    fun parseBid(line: String): Int = line.split(" ")[1].toInt()

    fun recode(content: List<MCardType>): Map<MCardType, Int> {
        val res = mutableMapOf<MCardType, Int>()
        for (cardType in content) {
            res.putIfAbsent(cardType, 0)
            res[cardType] = res[cardType]!! + 1
        }
        return res.toMap()
    }

    fun createHand(content: List<MCardType>, bid: Int): Hand {

        val distr = recode(content)


        var handType = when {
            distr.keys.size == 1 -> FIVE
            distr.keys.size == 2 && distr.values.any { it == 4 } -> FOUR
            distr.keys.size == 2 -> HOUSE
            distr.keys.size == 3 && distr.values.any { it == 3 } -> THREE
            distr.keys.size == 3 -> TWO_PAIR
            distr.keys.size == 4 -> ONE_PAIR
            else -> HIGH
        }

        val jokersCount = distr[MCardType.J]
        handType = when (jokersCount) {
            null -> handType
            1 -> when(handType) {
                FIVE -> throw IllegalStateException("J FIVE")
                FOUR -> FIVE
                HOUSE -> throw IllegalStateException("J HOUSE")
                THREE -> FOUR // J2333
                TWO_PAIR -> HOUSE // 2233J
                ONE_PAIR -> THREE // JKK23
                HIGH -> ONE_PAIR // J2345
            }
            2 -> when (handType) {
                FIVE -> throw IllegalStateException("JJ FIVE")
                FOUR -> throw IllegalStateException("JJ FOUR")
                HOUSE -> FIVE // JJ333
                THREE -> throw IllegalStateException("JJ THREE")
                TWO_PAIR -> FOUR // JJ334
                ONE_PAIR -> THREE // JJ234
                HIGH -> throw IllegalStateException("JJ HIGH")
            }
            3 -> when (handType) {
                FIVE -> throw IllegalStateException("JJJ FIVE")
                FOUR -> throw IllegalStateException("JJJ FOUR")
                HOUSE -> FIVE // JJJ22
                THREE -> FOUR // JJJ23
                TWO_PAIR -> throw IllegalStateException("JJJ TWO_PAIR")
                ONE_PAIR -> throw IllegalStateException("JJJ ONE_PAIR")
                HIGH -> throw IllegalStateException("JJJ HIGH")
            }
            4 -> FIVE
            5 -> FIVE
            else -> throw IllegalStateException("bad logic")
        }

        val sb = StringBuilder()
        sb.append(handType.strength)
        for (cardType in content) {
            sb.append(cardType.strength)
        }

        return Hand(content, bid, handType, sb.toString().toLong(16))
    }

    fun part2(input: List<String>): Long {
        val hands = input.asSequence().map {
            createHand(parseContent(it), parseBid(it))
        }.toList()


        val baseForResult = hands.sortedBy { it.strength }

        var result = 0L
        for ((index, item) in baseForResult.withIndex()) {
            val rank = index + 1
            println(" ${item.strength.toHexString()} $item rank str $rank")
            result += item.bid * rank
        }

        return result
    }

    val testInput2 = readInput("Day07_test")
    val part2Test = part2(testInput2)
    println("part2Test = $part2Test")
    check(part2Test == 5905L)

    val input = readInput("Day07")
    println("part2 = ${part2(input)}")
    // 250321350 too low
}
