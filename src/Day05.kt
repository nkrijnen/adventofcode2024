fun main() {
    fun part1(input: List<String>): Int {
        val rules = Rules(input)
        val updates = Updates(input)

        val validUpdates = updates.onlyValid(rules)
        return validUpdates.sumOf { it.middleNumber() }
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
//    check(part2(testInput) == ?)

    val input = readInput("Day05")
    part1(input).println()
//    part2(input).println()
}

class Rules(input: List<String>) {
    private val rules: List<Rule> = input
        .filter { it.contains("|") }
        .map { Rule(it) }

    fun isValidOrdering(page: Int, laterPage: Int): Boolean {
        return rules.any { it.matches(page, laterPage) } ||
                rules.all { !it.isForPages(page, laterPage) }
    }
}

fun Rule(line: String) = line.split("|")
    .map { it.toInt() }
    .let { Rule(it[0], it[1]) }

class Rule(private val before: Int, private val after: Int) {
    fun matches(page: Int, laterPage: Int): Boolean = page == before && after == laterPage
    fun isForPages(page: Int, laterPage: Int): Boolean =
        page == before || laterPage == after || laterPage == before || page == after
}

class Updates(input: List<String>) {
    private val updates: List<Update> = input
        .filter { it.contains(",") }
        .map { Update(it.split(",").map { it.toInt() }) }

    fun onlyValid(rules: Rules): List<Update> = updates.filter { it.isValid(rules) }
}

class Update(private val pages: List<Int>) {
    fun isValid(rules: Rules): Boolean {
        val pageSets: List<Pair<Int, Int>> = pages.subList(0, pages.size - 1).mapIndexed { index, page ->
            page to pages[index + 1]
        }
        return pageSets.all { (page, nextPage) -> rules.isValidOrdering(page, nextPage) }
    }

    fun middleNumber(): Int = pages[pages.size / 2]
}
