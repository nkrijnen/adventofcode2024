import kotlin.math.abs

fun main() {
    fun parseInput(input: List<String>): String = input.joinToString("")

    val mulPattern = "mul\\((\\d{1,3}),(\\d{1,3})\\)".toRegex()
    val mulAndDoPattern = "mul\\((\\d{1,3}),(\\d{1,3})\\)|do\\(\\)|don't\\(\\)".toRegex()

    fun part1(input: String):Int =
        mulPattern.findAll(input)
            .map { it.groupValues[1].toInt() * it.groupValues[2].toInt() }
            .sum()

    fun part2(input: String): Int {
        var enabled = true
        return mulAndDoPattern.findAll(input)
            .map { match ->
                if (match.value == "do()")
                    enabled = true
                else if (match.value == "don't()")
                    enabled = false
                else {
                    // == mul(..,..)
                    if (enabled) {
                        return@map match.groupValues[1].toInt() * match.groupValues[2].toInt()
                    }
                }
                return@map 0
            }
            .sum()
    }

    check(part1("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))\n") == 161)
    check(part2("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))") == 48)

    val input = readInput("Day03")
    part1(parseInput(input)).println()
    part2(parseInput(input)).println()
}
