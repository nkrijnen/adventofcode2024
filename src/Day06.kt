import Direction.*

fun main() {
    fun part1(input: List<String>): Int {
        println("grid size: ${input[0].length} x ${input.size}")
        val room = Room(input)
        return room.countVisitedPositions()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day06_test")
    check(Room(testInput).guardStartingPosition == Position(4, 6))

    check(Room(testInput).contains(Position(4, 6)))
    check(!Room(testInput).contains(Position(-1, 0)))
    check(Room(testInput).contains(Position(0, 9)))
    check(!Room(testInput).contains(Position(0, 10)))

    check(Room(testInput).hasObstacleAt(Position(4, 0)))
    check(!Room(testInput).hasObstacleAt(Position(3, 0)))

    check(part1(testInput) == 41)
//    check(part2(testInput) == ?)

    val input = readInput("Day06")
    part1(input).println()
//    part2(input).println()
}

class Room(private val input: List<String>) {
    val guardStartingPosition: Position = findStart()

    fun countVisitedPositions(): Int = guardPath().distinct().count()

    fun contains(p: Position): Boolean = p.x >= 0 && p.y >= 0 && p.x < input.size && p.y < input.size

    fun hasObstacleAt(position: Position): Boolean = input[position.y][position.x] == '#'

    private fun findStart(): Position {
        for (y in input.indices) {
            for (x in input[y].indices) {
                if (input[y][x] == '^') return Position(x, y)
            }
        }
        throw IllegalArgumentException("Can't find starting position")
    }

    private fun guardPath(): List<Position> {
        return Guard(this).pathUntilLeavesRoom()
    }
}

class Guard(private val room: Room) {
    private var position = room.guardStartingPosition
    private var direction = N

    fun pathUntilLeavesRoom(): List<Position> {
        val path = mutableListOf(position)
        while (true) {
            println(this)
            val nextPosition = position.movedIn(direction)
            if (!room.contains(nextPosition)) {
                break
            }
            if (room.hasObstacleAt(nextPosition)) {
                direction = direction.rotate90Clockwise()
            } else {
                position = nextPosition
                path.add(position)
            }
        }
        return path.toList()
    }

    override fun toString() = "${position.x},${position.y} -> ${direction}"
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
