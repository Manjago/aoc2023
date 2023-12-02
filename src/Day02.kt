import java.math.BigInteger

fun main() {

    val dict = mapOf("red" to 12, "green" to 13, "blue" to 14)

    fun String.allowed() : Int? {

        val gameId = this.substringAfter("Game ").substringBefore(":").toInt();
        val gameContent = this.substringAfter(": ");
        val attempts: List<String> = gameContent.split(";")

        for(attempt in attempts) {
            val tokens: List<String> = attempt.split(",")
            for(token in tokens) {
                val rawDigitColor = token.trim()
                val digitAndColor: List<String> = rawDigitColor.split(" ")
                val digit = digitAndColor[0].toInt()
                val color = digitAndColor[1]
                val fromDict: Int? = dict[color]
                if (fromDict == null) {
                    //println("Game $gameId deny - no color $color")
                    return null
                }
                if (digit > fromDict) {
                    //println("Game $gameId deny - $digit > $fromDict for color $color")
                    return null
                }
            }
        }

        return gameId;
    }

    fun part1(input: List<String>) = input.asSequence().map { it.allowed() }
        .filterNotNull().sum()

    fun String.power() : Long {

        val gameId = this.substringAfter("Game ").substringBefore(":").toInt();
        val gameContent = this.substringAfter(": ");
        val attempts: List<String> = gameContent.split(";")

        val minDict = mutableMapOf<String, Int>()

        for(attempt in attempts) {
            val tokens: List<String> = attempt.split(",")
            for(token in tokens) {
                val rawDigitColor = token.trim()
                val digitAndColor: List<String> = rawDigitColor.split(" ")
                val digit = digitAndColor[0].toInt()
                val color = digitAndColor[1]
                minDict.putIfAbsent(color, 0)
                val prevValue = minDict[color]!!
                if (digit > prevValue) {
                   minDict[color] = digit
                }
            }
        }

        var res = 1L
        for(value in minDict.values) {
            res *= value.toLong()
        }
        return res
    }

    fun part2(input: List<String>): Long = input.asSequence().map { it.power() }
        .sum()


    val testInput = readInput("Day02_test")
    val part1Test = part1(testInput)
    println("part1Test = $part1Test")
    check(part1Test == 8)

    val part2Test = part2(testInput)
    println("part2Test = $part2Test")
    check(part2Test == 2286L)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
