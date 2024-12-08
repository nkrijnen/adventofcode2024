fun main() {
    fun part1(input: List<String>): Int = CityMap(input).uniqueAntinodeLocations()

    fun part2(input: List<String>): Int = CityMap(input).uniqueAntinodeLineLocations()

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput("Day08")
    val part1result = part1(input)
    part1result.println()
    part2(input).println()
}

private class CityMap(private val grid: List<String>) {
    private val width: Int = grid.first().length
    private val height: Int = grid.size

    fun uniqueAntinodeLocations(): Int {
        val allStations: List<Station> = findStations()
        val stationsByType = allStations.groupBy { it.type }
        val antiNodes = uniqueAntiNodes(stationsByType)
        val antiNodesWithinCity = antiNodes.filter { it.withinCityBounds() }.toSet()
        printWithOverlay(antiNodesWithinCity)
        return antiNodesWithinCity.size
    }

    fun uniqueAntinodeLineLocations(): Int {
        val allStations: List<Station> = findStations()
        val stationsByType = allStations.groupBy { it.type }
        val antiNodesWithinCity = uniqueAntiNodesOnLine(stationsByType)
        printWithOverlay(antiNodesWithinCity)
        return antiNodesWithinCity.size
    }

    private fun printWithOverlay(vectors: Set<Vector>) {
        val overlay: List<String> = grid.mapIndexed { y, row ->
            row.mapIndexed { x, c ->
                if (c != '.' && vectors.contains(Vector(x, y))) '%'
                else if (vectors.contains(Vector(x, y))) '#'
                else c
            }.joinToString("")
        }
        overlay.forEach { row -> row.println() }
    }

    private fun uniqueAntiNodes(stationsByType: Map<Char, List<Station>>) =
        stationsByType.flatMap { (_, stations) ->
            val stationCombinations = stationCombinations(stations)
            val antiNodes = stationCombinations.flatMap { it.locations().antiNodes() }
            antiNodes
        }.toSet()

    private fun uniqueAntiNodesOnLine(stationsByType: Map<Char, List<Station>>) =
        stationsByType.flatMap { (_, stations) ->
            val stationCombinations = stationCombinations(stations)
            val antiNodes = stationCombinations.flatMap { it.locations().antiNodesOnLine() }
            antiNodes
        }.toSet()

    private fun Pair<Vector, Vector>.antiNodesOnLine(): List<Vector> {
        val delta: Vector = first - second
        val nodes = mutableListOf<Vector>(first)
        while (true) {
            val next = nodes.last() + delta
            if (!next.withinCityBounds()) break
            nodes.add(next)
        }
        nodes.add(second)
        while (true) {
            val next = nodes.last() - delta
            if (!next.withinCityBounds()) break
            nodes.add(next)
        }
        return nodes
    }

    fun findStations(): List<Station> {
        return grid.flatMapIndexed { y: Int, row: String ->
            row.mapIndexedNotNull { x: Int, c: Char ->
                if (c == '.') null
                else Station(c, Vector(x, y))
            }
        }
    }

    fun Vector.withinCityBounds(): Boolean = x in 0..width - 1 && y in 0..height - 1
}

// a, b, c =  ab, ac,  bc
// a, b, c, d =  ab, ac, ad,  bc, bd,  cd
private fun stationCombinations(stations: List<Station>): List<Pair<Station, Station>> {
    require(stations.size > 1) { "Insufficient stations: $stations" }
    return stations.flatMapIndexed { index, a ->
        stations.drop(index + 1).map { b ->
            a to b
        }
    }
}

//.#....
//..A...
//...A..
//....#.
// A1 2,1
// A2 3,2
// D = A1 - A2 = 2,1 - 3,2 = -1,-1
// #1 = A1 - D = 2,1 + -1,-1 = 1,0
// #2 = A2 + D = 3,2 - -1,-1 = 4,3
private fun Pair<Vector, Vector>.antiNodes(): List<Vector> {
    val delta: Vector = first - second
    return listOf(
        first + delta,
        second - delta
    )
}

private data class Station(
    val type: Char,
    val location: Vector
) {
    override fun toString() = "$type$location"
}

private fun Pair<Station, Station>.locations(): Pair<Vector, Vector> = first.location to second.location

private data class Vector(val x: Int, val y: Int) {
    operator fun plus(other: Vector): Vector = Vector(this.x + other.x, this.y + other.y)
    operator fun minus(other: Vector): Vector = Vector(this.x - other.x, this.y - other.y)
    override fun toString() = "($x, $y)"
}
