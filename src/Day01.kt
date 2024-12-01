import kotlin.math.abs

fun parseInput(input: List<String>): Pair<List<Int>, List<Int>> = input.map {
    it.split("   ")
        .let { it[0].toInt() to it[1].toInt() }
}.unzip()

fun main() {
    fun part1(input: List<String>): Int {
        val (left, right) = parseInput(input)
        val sortedInput = left.sorted().zip(right.sorted())
        val distances = sortedInput.map { abs(it.second - it.first) }
        return distances.sum()
    }

    fun part2(input: List<String>): Int {
        val (left, right) = parseInput(input)
        val similarityScores = left.map { nr -> nr * right.count { it == nr } }
        return similarityScores.sum()
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("test_input")) == 6)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
