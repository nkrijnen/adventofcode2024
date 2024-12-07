import Direction.*

fun main() {
    fun part1(input: List<String>): Int {
        val room = Room(input)
        return room.countVisitedPositions()
    }

    fun part2(input: List<String>): Int {
        val room = Room(input)
        return room.nrOfPositionForObstructionToGetGuardStuck()
    }

    val testInput = readInput("Day06_test")
    check(findStart(testInput) == Position(4, 6))

    check(Room(testInput).contains(Position(4, 6)))
    check(!Room(testInput).contains(Position(-1, 0)))
    check(Room(testInput).contains(Position(0, 9)))
    check(!Room(testInput).contains(Position(0, 10)))

    check(Room(testInput).hasObstacleAt(Position(4, 0)))
    check(!Room(testInput).hasObstacleAt(Position(3, 0)))

    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

private fun findStart(input: List<String>): Position {
    for (y in input.indices) {
        for (x in input[y].indices) {
            if (input[y][x] == '^') return Position(x, y)
        }
    }
    throw IllegalArgumentException("Can't find starting position")
}

class Room(
    private val input: List<String>,
    private val guardStartingPosition: Position = findStart(input),
    private val obstructionAt: Position? = null
) {
    fun countVisitedPositions(): Int = guardPath()!!.distinct().count()

    fun nrOfPositionForObstructionToGetGuardStuck(): Int {
        return possibleObstructionLocations().count { obstruction ->
            Room(input, guardStartingPosition, obstruction).doesGuardGetsStuck()
        }
    }

    internal fun contains(p: Position): Boolean = p.x >= 0 && p.y >= 0 && p.x < input.size && p.y < input.size

    internal fun hasObstacleAt(position: Position): Boolean =
        input[position.y][position.x] == '#' || hasObstructionAt(position)

    private fun hasObstructionAt(position: Position): Boolean = when {
        obstructionAt != null -> obstructionAt == position
        else -> false
    }

    private fun guardPath(): List<Position>? {
        return Guard(this, guardStartingPosition).pathUntilLeavesRoom()
    }

    private fun doesGuardGetsStuck(): Boolean = guardPath() == null

    private fun possibleObstructionLocations(): List<Position> = input.flatMapIndexed { y, line ->
        line.mapIndexed { x, c -> if (c == '.') Position(x, y) else null }
    }.filterNotNull()
}

class Guard(private val room: Room, private var position: Position) {
    private var direction = N

    fun pathUntilLeavesRoom(): List<Position>? {
        val path = mutableListOf(position)
        while (true) {
            val nextPosition = position.movedIn(direction)
            if (!room.contains(nextPosition)) {
                break
            }
            if (room.hasObstacleAt(nextPosition)) {
                direction = direction.rotate90Clockwise()
            } else {
                if (path.isLooping(position, nextPosition)) {
                    println("looping")
                    return null
                }
                position = nextPosition
                path.add(position)
            }
        }
        return path.toList()
    }

    override fun toString() = "${position.x},${position.y} -> $direction"
}

// get stuck detection = check if pos & next (on pos change) already appear as pair in path history
private fun List<Position>.isLooping(position: Position, nextPosition: Position): Boolean {
    var index = size
    while (--index > 0) {
        if (this[index] == nextPosition && this[index - 1] == position) {
            return true
        }
    }
    return false
}

enum class Direction {
    N, E, S, W;

    fun rotate90Clockwise(): Direction = when (this) {
        N -> E
        E -> S
        S -> W
        W -> N
    }
}

data class Position(val x: Int, val y: Int) {
    fun movedIn(direction: Direction): Position = when (direction) {
        N -> copy(y = y - 1)
        S -> copy(y = y + 1)
        E -> copy(x = x + 1)
        W -> copy(x = x - 1)
    }
}
