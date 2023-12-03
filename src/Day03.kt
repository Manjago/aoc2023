fun main() {

    fun ArrayDeque<Char>.toInt(): Int {
        var mult = 1
        var result = 0
        while (this.isNotEmpty()) {
            val char = this.removeLast()
            val digit = char - '0'
            result += digit * mult
            mult *= 10
        }
        return result
    }

    fun Char.isEngineMarker() = !this.isDigit() && this != '.'

    fun max(a: Int, b: Int) = if (a > b) a else b
    fun min(a: Int, b: Int) = if (a < b) a else b

    fun List<String>.isEnginePart(lineIndex: Int, startNumberIndex: Int, endNumberIndex: Int): Boolean {
        if ((lineIndex - 1) >= 0) {

            val realStart = max(0, startNumberIndex - 1)
            val realEnd = min(endNumberIndex + 1, this[lineIndex].length)

            if (this[lineIndex - 1].slice(realStart..realEnd).toCharArray().asSequence().any { it.isEngineMarker() }) {
                return true
            }
        }
        if ((lineIndex + 1) <= (this.size - 1)) {
            val realStart = max(0, startNumberIndex - 1)
            val realEnd = min(endNumberIndex + 1, this[lineIndex].length)

            if (this[lineIndex + 1].slice(realStart..realEnd).toCharArray().asSequence().any { it.isEngineMarker() }) {
                return true
            }
        }
        if ((startNumberIndex - 1) >= 0) {
            if (this[lineIndex][startNumberIndex - 1].isEngineMarker()) {
                return true
            }
        }
        if ((endNumberIndex + 1) <= (this[lineIndex].length - 1)) {
            if (this[lineIndex][endNumberIndex + 1].isEngineMarker()) {
                return true
            }
        }
        return false
    }

    fun checkAndInc(
        numberContent: ArrayDeque<Char>, input: List<String>, lineIndex: Int, startIndex: Int, endIndex: Int
    ): Int = if (!numberContent.isEmpty()) {
        val pretender = numberContent.toInt() // clean numberContent
        if (input.isEnginePart(lineIndex, startIndex, endIndex)) {
            println("Number $pretender is good")
            pretender
        } else {
            println("Number $pretender is bad")
            0
        }
    } else {
        0
    }

    fun part1(input: List<String>): Int {

        var result = 0

        for ((lineIndex, line) in input.withIndex()) {

            val numberContent = ArrayDeque<Char>()
            var startIndex = -1
            var endIndex = -1

            for ((charIndex, char) in line.toCharArray().withIndex()) {
                if (char.isDigit()) {
                    numberContent.add(char)
                    if (startIndex == -1) {
                        startIndex = charIndex
                    }
                } else {
                    if (startIndex != -1) {
                        endIndex = charIndex - 1
                    }
                    result += checkAndInc(numberContent, input, lineIndex, startIndex, endIndex)
                    startIndex = -1
                    endIndex = -1
                }
            }
            result += checkAndInc(numberContent, input, lineIndex, startIndex, endIndex)
        }

        return result
    }

    data class Point(val row: Int, val column: Int)

    fun List<String>.tryAddStar(
        numberContent: ArrayDeque<Char>,
        lineIndex: Int,
        startNumberIndex: Int,
        endNumberIndex: Int,
        stars: MutableMap<Point, MutableList<Int>>
    ) {
        val pretender = numberContent.toInt() // clean numberContent

        if ((lineIndex - 1) >= 0) {

            val realStart = max(0, startNumberIndex - 1)
            val realEnd = min(endNumberIndex + 1, this[lineIndex].length)

            for(i in realStart..realEnd) {
                val char = this[lineIndex - 1][i]
                if (char  == '*') {
                    val point = Point(lineIndex - 1, i)
                    stars.putIfAbsent(point, mutableListOf())
                    stars[point]!!.add(pretender)
                }
            }
        }
        if ((lineIndex + 1) <= (this.size - 1)) {
            val realStart = max(0, startNumberIndex - 1)
            val realEnd = min(endNumberIndex + 1, this[lineIndex].length)

            for(i in realStart..realEnd) {
                val char = this[lineIndex + 1][i]
                if (char  == '*') {
                    val point = Point(lineIndex + 1, i)
                    stars.putIfAbsent(point, mutableListOf())
                    stars[point]!!.add(pretender)
                }
            }
        }
        if ((startNumberIndex - 1) >= 0) {
            if (this[lineIndex][startNumberIndex - 1] == '*') {
                val point = Point(lineIndex, startNumberIndex - 1)
                stars.putIfAbsent(point, mutableListOf())
                stars[point]!!.add(pretender)
            }
        }
        if ((endNumberIndex + 1) <= (this[lineIndex].length - 1)) {
            if (this[lineIndex][endNumberIndex + 1] == '*') {
                val point = Point(lineIndex, endNumberIndex + 1)
                stars.putIfAbsent(point, mutableListOf())
                stars[point]!!.add(pretender)
            }
        }

    }

    fun part2(input: List<String>): Int {
        val stars = mutableMapOf<Point, MutableList<Int>>()

        for ((lineIndex, line) in input.withIndex()) {

            val numberContent = ArrayDeque<Char>()
            var startIndex = -1
            var endIndex = -1

            for ((charIndex, char) in line.toCharArray().withIndex()) {
                if (char.isDigit()) {
                    numberContent.add(char)
                    if (startIndex == -1) {
                        startIndex = charIndex
                    }
                } else {
                    if (startIndex != -1) {
                        endIndex = charIndex - 1
                    }
                    input.tryAddStar(numberContent, lineIndex, startIndex, endIndex, stars)
                    startIndex = -1
                    endIndex = -1
                }
            }
            input.tryAddStar(numberContent, lineIndex, startIndex, endIndex, stars)
        }

        return stars.values.asSequence()
            .filter { it.size == 2 }
            .map { it[0] * it[1] }
            .sum()
    }

    val testInput = readInput("Day03_test")
    val part1Test = part1(testInput)
    println("part1Test = $part1Test")
    check(part1Test == 4361)

    val testInput2 = readInput("Day03_test")
    val part2Test = part2(testInput2)
    println("part2Test = $part2Test")
    check(part2Test == 467835)

    val input = readInput("Day03")
    println("part1 = ${part1(input)}")
    println("part2 = ${part2(input)}")
}
