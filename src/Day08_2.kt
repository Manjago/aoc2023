fun main() {

    data class LeftRight(val from: String, val left: String, val right: String)

    data class Process(
        var from: String
    )

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
        input.asSequence().drop(2).map { parseOneMazePass(it) }.map { it.from to it }.toMap()

    fun depth(process: Process, maze: Map<String, LeftRight>, instruction: String): Long {
        var counter = 0L
        var found = false
        while (!found) {
            for (char in instruction.toCharArray()) {

                val next = when (char) {
                    'L' -> maze[process.from]!!.left
                    'R' -> maze[process.from]!!.right
                    else -> throw IllegalStateException("Bad instruction $char")
                }
                process.from = next

                ++counter

                val zEnded = process.from.endsWith("Z")
                if (zEnded) {
                    found = true
                    break
                }
            }
        }
        return counter
    }

    fun part2(input: List<String>): Long {

        val instruction = parseInstruction(input)
        val maze = parseMaze(input)

        return maze.keys.asSequence().filter {
            it.endsWith("A")
        }.map {
            Process(it)
        }.map {
            depth(it, maze, instruction)
        }.reduce { acc, l -> lcm(acc, l) }

    }

    val testInput = readInput("Day08_test_2")
    val part2Test = part2(testInput)
    println("part2Test = $part2Test")
    check(part2Test == 6L)

    val input = readInput("Day08")
    println("part2 = ${part2(input)}")
}
