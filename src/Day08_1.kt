fun main() {

    data class LeftRight(val from: String, val left: String, val right: String)

    fun parseInstruction(input: List<String>): String = input[0]

    fun parseOneMazePass(source: String): LeftRight {
        // AAA = (BBB, BBB)
        val tokens = source.split("=")
        val from = tokens[0].trim()
        val left = tokens[1].substringAfter("(").substringBefore(",").trim()
        val right = tokens[1].substringAfter(",").substringBefore(")").trim()
        return LeftRight(from, left, right)
    }

    fun parseMaze(input: List<String>): Map<String, LeftRight> =
        input.asSequence().drop(2).map { parseOneMazePass(it) }
            .map { it.from to it }.toMap()

    fun part1(input: List<String>): Int {

        val instruction = parseInstruction(input)
        val maze = parseMaze(input)

        var counter = 0
        var from = "AAA"
        var found = false

        while(!found) {
            for(char in instruction.toCharArray()) {
                val next = when(char) {
                    'L' -> maze[from]!!.left
                    'R' -> maze[from]!!.right
                    else -> throw IllegalStateException("Bad instruction $char")
                }
                ++counter
                if (next == "ZZZ") {
                    found = true
                    break
                }
                from = next
            }
        }

        return counter
    }

    val testInput = readInput("Day08_test")
    val part1Test = part1(testInput)
    println("part1Test = $part1Test")
    check(part1Test == 6)

    val input = readInput("Day08")
    println("part1 = ${part1(input)}")
}
