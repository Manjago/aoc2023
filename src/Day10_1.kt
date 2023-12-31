
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

    fun Point.step(mazeDir: MazeDir): Point = when (mazeDir) {
        N -> Point(x, y - 1)
        E -> Point(x + 1, y)
        S -> Point(x, y + 1)
        W -> Point(x - 1, y)
    }

    class Maze(private val data: Array<Array<MazeCell>>) {

        fun isValidPoint(point: Point) = point.isValid()

        fun Point.isValid(): Boolean = this.y in data.indices && this.x in data[0].indices

        fun Point.isAllowToDir(mazeDir: MazeDir): Boolean = (mazeDir.bit and data[y][x]) != 0

        fun step(current: Point, from: Point?): Point {

            lateinit var result: Point

            for (dir in MazeDir.VALUES) {
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

        fun getWidth() = data[0].size

        fun getHeight() = data.size

        fun getCell(x: Int, y: Int): MazeCell = data[y][x]
        fun getCell(point: Point): MazeCell = getCell(point.x, point.y)
    }

    fun parseChar(char: Char, substForS: Char): MazeCell {
        return when (char) {
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

    fun parseStart(input: List<String>): Point {
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
        var prev: Point? = null
        var current = start

        do {
            val savedCurrent = current
            current = maze.step(savedCurrent, prev)
            prev = savedCurrent
            ++len
        } while (current != start)

        return len / 2
    }

    fun toPolygon(loop: List<Point>, maze: Maze): List<Point> = loop.asSequence().filter {
        val cell = maze.getCell(it)
        val hor = cell == E.bit + W.bit
        val ver = cell == N.bit + S.bit
        !(hor || ver)
    }.toList()

    fun part2(input: List<String>): Int {
        val maze = Maze(parseMaze(input, 'F'))
        val start = parseStart(input)
        var len = 0
        var prev: Point? = null
        var current = start
        val loop = mutableListOf<Point>()
        val loopSet = mutableSetOf<Point>()

        do {
            val savedCurrent = current
            current = maze.step(savedCurrent, prev)
            loop.add(current)
            loopSet.add(current)
            prev = savedCurrent
            ++len
        } while (current != start)

        val polygon = toPolygon(loop, maze)

        val queue = ArrayDeque<Point>()

        // all border points
        val height = maze.getHeight()
        val width = maze.getWidth()

        for (x in 0 until width) {
            for (y in 0 until height) {
                val point = Point(x, y)
                if (loopSet.contains(point)) {
                    continue
                }
                if (point.x == 0 || point.x == (width - 1) || point.y == 0 || point.y == (height - 1)) {
                    queue += point
                }
            }
        }

        val shifts = listOf(Point(1, 0), Point(-1, 0), Point(0, 1), Point(0, -1))
        val visited = mutableSetOf<Point>()
        while(queue.isNotEmpty()) {
            val point = queue.removeFirst()
            if (visited.contains(point)) {
                continue
            }
            visited.add(point)

            for(shift in shifts) {
                val pretender = point + shift
                if (maze.isValidPoint(pretender) && !loopSet.contains(point)) {
                    queue += pretender
                }
            }
        }

        val inLoop = mutableListOf<Point>()
        for (x in 0 until width) {
            for (y in 0 until height) {
                val point = Point(x, y)
                if (loopSet.contains(point)) {
                    continue
                }
                if (visited.contains(point)) {
                    continue
                }
                inLoop += point
            }
        }

        println("inLoop = $inLoop")
        return inLoop.size
    }

    val testInput = readInput("Day10_test")
    val part1Test = part1(testInput)
    println("part1Test = $part1Test")
    check(part1Test == 8)

    val input = readInput("Day10")
    val part1 = part1(input)
    println("part1 = $part1")
    check(part1 == 6690)

    val part2 = part2(readInput("Day10_test2"))
    println("part2 = $part2")
}

