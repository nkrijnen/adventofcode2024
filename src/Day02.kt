private class Report(private val levels: List<Int>) {
    fun isSafe(): Boolean {
        var level = levels[0]
        val ascending = level < levels[1];
        for (nextLevel in levels.subList(1, levels.size)) {
            if (nextLevel == level)
                return false
            if (ascending && nextLevel < level)
                return false
            if (!ascending && nextLevel > level)
                return false
            if (nextLevel > level + 3)
                return false
            if (nextLevel < level - 3)
                return false
            level = nextLevel
        }
        return true
    }

    override fun toString() = levels.joinToString("•") + " safe: " + isSafe()
}

fun main() {
    fun parseInput(input: List<String>): List<Report> = input
        .map { it.split(" ").map { it.toInt() } }
        .map { Report(it) }

    fun part1(input: List<String>): Int {
        val reports = parseInput(input)
        reports.forEach { it.println() }
        return reports.count { it.isSafe() }
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    check(part1(listOf("30 29 27 26 24 23 21 21")) == 0)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
//    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day02")
    part1(input).println()
//    part2(input).println()
}
