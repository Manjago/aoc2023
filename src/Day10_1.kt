import MazeDir.E
import MazeDir.N
import MazeDir.S
import MazeDir.W

typealias MazeCell = Int

/*
line 0 01234567
line 1 01234567
line 2 01234567

      N(1)
 W(8)      E(2)
      S(4)
 */
enum class MazeDir(val bit: Int) {
    N(1), E(2), S(4), W(8);
    companion object {
        val VALUES = entries.toTypedArray()
    }
}
fun main() {

    data class Point(val x: Int, val y: Int) {
        fun step(mazeDir: MazeDir): Point = when(mazeDir) {
            N -> Point(x, y - 1)
            E -> Point(x + 1, y)
            S -> Point(x, y + 1)
            W -> Point(x - 1, y)
        }
    }

    class Maze(private val data: Array<Array<MazeCell>>) {

        fun Point.isValid(): Boolean = this.y in data.indices && this.x in data[0].indices

        fun Point.isAllowToDir(mazeDir: MazeDir): Boolean = (mazeDir.bit and data[y][x]) != 0

        fun step(current: Point, from: Point?): Point {

            lateinit var result: Point

            for(dir in MazeDir.VALUES) {
                if (!current.isAllowToDir(dir)) {
                    continue
                }
                result = current.step(dir)
                if (result.isValid() && result != from) {
                    return result
                }
            }

            throw IllegalStateException("current $current from $from")
        }
    }

    fun parseChar(char: Char, substForS: Char): MazeCell {
        return when(char) {
            '|' -> N.bit + S.bit
            '-' -> W.bit + E.bit
            'L' -> N.bit + E.bit
            'J' -> N.bit + W.bit
            '7' -> W.bit + S.bit
            'F' -> E.bit + S.bit
            '.' -> 0
            'S' -> parseChar(substForS, substForS)
            else -> throw IllegalArgumentException("$char")
        }
    }

    fun parseMaze(input: List<String>, substForS: Char): Array<Array<MazeCell>> {
        val height = input.size
        val width = input[0].length
        val result = Array(height) { Array(width) { 0 } }

        for (y in input.indices) {
            val chars = input[y].toCharArray()
            for (x in chars.indices) {
                  result[y][x] = parseChar(chars[x], substForS)
            }
        }
        return result
    }

    fun parseStart(input: List<String>) : Point {
        for (y in input.indices) {
            val chars = input[y].toCharArray()
            for (x in chars.indices) {
                if (chars[x] == 'S') {
                    return Point(x, y)
                }
            }
        }
        throw IllegalStateException()
    }

    fun part1(input: List<String>): Int {
        val maze = Maze(parseMaze(input, 'F'))
        val start = parseStart(input)
        var len = 0
        var prev :Point? = null
        var current = start
        //println("current(start) = $current")

        do {
            val savedCurrent = current
            current = maze.step(savedCurrent, prev)
            //println("current = $current, start = $start")
            prev = savedCurrent
            ++len
        }while(current != start)

        return len / 2
    }

    val testInput = readInput("Day10_test")
    val part1Test = part1(testInput)
    println("part1Test = $part1Test")
    check(part1Test == 8)

    val input = readInput("Day10")
    println("part1 = ${part1(input)}")
}

