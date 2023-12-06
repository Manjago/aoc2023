fun main() {

    data class TimeDistance(val time: Int, val dist: Int)

    fun parse(input: List<String>) : List<TimeDistance> {
        val times = input[0].substringAfter("Time: ").trim().split(" ")
            .asSequence().map { it.trim() }.filter { it.isNotBlank() }
            .map { it.toInt() }.toList()
        val dists = input[1].substringAfter("Distance: ").trim().split(" ")
            .asSequence().map { it.trim() }.filter { it.isNotBlank() }
            .map { it.toInt() }.toList()
        check(times.size == dists.size)
        return times.indices.asSequence().map { TimeDistance(times[it], dists[it]) }.toList()
    }

    fun waysToWin(td: TimeDistance): List<Int> {
        val result = mutableListOf<Int>()
        for(i in 1..<td.time) {
            val restTime = td.time - i
            val distance = restTime * i
            if (distance > td.dist) {
                result.add(i)
            }
        }
        return result.toList()
    }

    fun part1(input: List<String>): Int = parse(input).asSequence().map {
        waysToWin(it).size
    }.reduce { acc, i -> i * acc }

    val testInput = readInput("Day06_test")
    val part1Test = part1(testInput)
    println("part1Test = $part1Test")
    check(part1Test == 288)

    val input = readInput("Day06")
    println("part1 = ${part1(input)}")
}
