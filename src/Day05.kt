fun main() {

    data class Mapper(val sourceRange: LongRange, val targetStart: Long, val rangeSize: Long)


    fun parseSeeds(input: List<String>): List<Long> =
        input[0].substringAfter("seeds: ").split(" ").asSequence().map { it.trim() }.map { it.toLong() }.toList()

    fun parseMapper(line: String): Mapper {
        val tokens = line.split(" ").asSequence().map { it.toLong() }.toList()
        return Mapper(LongRange(tokens[1], tokens[1] + tokens[2] - 1), tokens[0], tokens[2])
    }

    fun parseMappers(input: List<String>): Map<String, List<Mapper>> {

        val acc = mutableMapOf<String, List<Mapper>>()
        var currentMappers: MutableList<Mapper>? = null
        var currentName: String? = null

        for (line in input) {
            when {
                line.startsWith("seeds: ") || line.isBlank() -> continue
                line.contains("map:") -> {
                    if (currentName != null && currentMappers != null) {
                        acc.put(currentName, currentMappers.toList())
                    }

                    currentMappers = mutableListOf()
                    currentName = line.substringBefore(" map:")
                }

                else -> currentMappers!!.add(parseMapper(line))
            }
        }
        if (currentName != null && currentMappers != null) {
            acc.put(currentName, currentMappers.toList())
        }

        return acc.toMap()
    }

    fun recodeByMappers(value: Long, mappers: List<Mapper>): Long {
        val mapper = mappers.find {
            it.sourceRange.contains(value)
        } ?: return value

        val shift = value - mapper.sourceRange.first
        return mapper.targetStart + shift
    }

    fun part1(input: List<String>): Long {
        val seeds = parseSeeds(input)
        val mappers = parseMappers(input)

        return seeds.asSequence().map {

            var current = it
            current = recodeByMappers(current, mappers["seed-to-soil"]!!)
            current = recodeByMappers(current, mappers["soil-to-fertilizer"]!!)
            current = recodeByMappers(current, mappers["fertilizer-to-water"]!!)
            current = recodeByMappers(current, mappers["water-to-light"]!!)
            current = recodeByMappers(current, mappers["light-to-temperature"]!!)
            current = recodeByMappers(current, mappers["temperature-to-humidity"]!!)
            current = recodeByMappers(current, mappers["humidity-to-location"]!!)
            current
        }.min()
    }

    fun part2(input: List<String>): Long {
        return input.size.toLong()
    }

    val testInput = readInput("Day05_test")
    val part1Test = part1(testInput)
    println("part1Test = $part1Test")
    check(part1Test == 35L)

    val testInput2 = readInput("Day05_test")
    val part2Test = part2(testInput2)
    println("part2Test = $part2Test")
    check(part2Test == 33L)

    val input = readInput("Day05")
    println("part1 = ${part1(input)}")
    println("part2 = ${part2(input)}")
}
