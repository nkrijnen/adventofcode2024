import kotlin.math.abs

fun main() {
    fun parseInput(input: List<String>): String = input.joinToString("")

    val pattern = "mul\\((\\d{1,3}),(\\d{1,3})\\)".toRegex()

    fun part1(input: String):Int =
        pattern.findAll(input)
            .map { it.groupValues[1].toInt() * it.groupValues[2].toInt() }
            .sum()

    fun part2(input: String): Int {
        TODO()
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("test_input")) == 6)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day03_test")
    check(part1(parseInput(testInput)) == 161)
//    check(part2(parseInput(testInput)) == 48)

    val input = readInput("Day03")
    part1(parseInput(input)).println()
//    part2(parseInput(input)).println()
}
