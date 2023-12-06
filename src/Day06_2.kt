fun main() {

    data class LongTimeDistance(val time: Long, val dist: Long)

    fun parseSingle(input: List<String>) : LongTimeDistance {
        val time = input[0].substringAfter("Time: ").trim().split(" ")
            .asSequence().map { it.trim() }.filter { it.isNotBlank() }
            .joinToString("")
        val dist = input[1].substringAfter("Distance: ").trim().split(" ")
            .asSequence().map { it.trim() }.filter { it.isNotBlank() }
            .joinToString("")
        println("parse times=$time, dists=$dist")
        return LongTimeDistance(time.toLong(), dist.toLong())
    }

    fun LongTimeDistance.isWin(time: Long): Boolean {
        val restTime = this.time - time
        val distance = restTime * time
        return (distance > this.dist)
    }

    fun findLeftBoundIndex(td: LongTimeDistance, leftInitial: Long, rightInitial: Long) : Long {
        var right =  rightInitial
        var left = leftInitial

        while(left < right) {
            val mid = (left + right) / 2
            if (mid == left || mid == right) {
                break
            }
            val midWin = td.isWin(mid)
            if (midWin) {
                right = mid
            } else {
                left = mid
            }
        }
        return right
    }

    fun findRightBoundIndex(td: LongTimeDistance, leftInitial: Long, rightInitial: Long) : Long {
        var right =  rightInitial
        var left = leftInitial

        while(left < right) {
            val mid = (left + right) / 2
            if (mid == left || mid == right) {
                break
            }
            val midWin = td.isWin(mid)
            if (midWin) {
                left = mid
            } else {
                right = mid
            }
        }
        return left
    }

    fun part2(input: List<String>): Long {
        val td = parseSingle(input)

        val leftBoundIndex = findLeftBoundIndex(td, 0L, td.time / 2)
        println("leftIndex = $leftBoundIndex")
        val rightBoundIndex = findRightBoundIndex(td, td.time / 2, td.time)
        println("rightIndex = $rightBoundIndex")
        return rightBoundIndex - leftBoundIndex + 1
    }

    val testInput2 = readInput("Day06_test")
    val part2Test = part2(testInput2)
    println("part2Test = $part2Test")
    check(part2Test == 71503L)

    val input = readInput("Day06")
    println("part2 = ${part2(input)}")
}
