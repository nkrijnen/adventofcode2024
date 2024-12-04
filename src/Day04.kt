class XMASPatternFinder(
    private val grid: List<String>,
    private val wordToFind: String = "XMAS",
    private val searchChar: Char = wordToFind[0]
) {
    private val wordLength: Int = wordToFind.length
    private val width: Int = grid.first().length
    private val height: Int = grid.size

    init {
        println("grid size: $width x $height")
    }

    fun findWordCoordinates(): List<Coordinate> {
        return grid.flatMapIndexed { y: Int, row: String ->
            row.mapIndexedNotNull { x: Int, char: Char ->
                if (char == searchChar) Coordinate(x, y)
                else null
            }
        }
    }

    inner class Coordinate(private val x: Int, private val y: Int) {
        init {
            require(x in 0..<width) { "X outside grid: x=$x, y=$y" }
            require(y in 0..<height) { "Y outside grid: x=$x, y=$y" }
        }

        internal fun char(): Char = grid[y][x]

        fun possibleStarPatterns(): List<CoordinateSet> {
            return listOf(
                east(),
                west(),
                north(),
                south(),
                southEast(),
                southWest(),
                northEast(),
                northWest(),
            )
                .filterNotNull()
                .filter { it.toString() == wordToFind }
        }

        private fun east() = CoordinateSet { delta -> Coordinate(x + delta, y) }
        private fun west() = CoordinateSet { delta -> Coordinate(x - delta, y) }
        private fun north() = CoordinateSet { delta -> Coordinate(x, y - delta) }
        private fun south() = CoordinateSet { delta -> Coordinate(x, y + delta) }
        private fun southEast() = CoordinateSet { delta -> Coordinate(x + delta, y + delta) }
        private fun southWest() = CoordinateSet { delta -> Coordinate(x - delta, y + delta) }
        private fun northEast() = CoordinateSet { delta -> Coordinate(x + delta, y - delta) }
        private fun northWest() = CoordinateSet { delta -> Coordinate(x - delta, y - delta) }

        fun isCrossPattern(): Boolean {
            return listOf(
                listOf(
                    nwToSe(),
                    swToNe(),
                ),
                listOf(
                    neToSw(),
                    seToNw(),
                ),
                listOf(
                    neToSw(),
                    nwToSe(),
                ),
                listOf(
                    swToNe(),
                    seToNw(),
                ),
            ).any { it.filterNotNull().count { it.toString() == wordToFind } == 2 }
        }

        private fun nwToSe() = CoordinateCross { delta -> Coordinate(x + delta, y + delta) }
        private fun neToSw() = CoordinateCross { delta -> Coordinate(x - delta, y + delta) }
        private fun swToNe() = CoordinateCross { delta -> Coordinate(x + delta, y - delta) }
        private fun seToNw() = CoordinateCross { delta -> Coordinate(x - delta, y - delta) }
    }

    private fun CoordinateSet(coordinateWithDelta: (Int) -> Coordinate): CoordinateSet? = try {
        CoordinateSet((0..wordLength - 1).map { delta -> coordinateWithDelta(delta) })
    } catch (e: IllegalArgumentException) {
        null
    }

    private fun CoordinateCross(coordinateWithDelta: (Int) -> Coordinate): CoordinateSet? = try {
        CoordinateSet((-1..1).map { delta -> coordinateWithDelta(delta) })
    } catch (e: IllegalArgumentException) {
        null
    }

    inner class CoordinateSet(private val coordinates: List<Coordinate>) {
        override fun toString() = coordinates.map { it.char() }.joinToString("")
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return XMASPatternFinder(input).findWordCoordinates()
            .flatMap { it.possibleStarPatterns() }
            .count()
//            .map { it.toString() }
//            .println()
    }

    fun part2(input: List<String>): Int {
        return XMASPatternFinder(input, "MAS", 'A').findWordCoordinates()
            .count { it.isCrossPattern() }
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
