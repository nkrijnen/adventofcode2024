import Operations.operations
import java.util.stream.Collectors

fun main() {
    fun part1(input: List<String>): Long {
        val equations = input.map(::Equation)
        return equations
            .parallelStream()
            .filter { it.isValid() }
            .collect(Collectors.summingLong { it.expectedResult })
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

//    check(part1(listOf("...")) == 1)

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)
//    check(part2(testInput) == ?)

    val input = readInput("Day07")
    part1(input).println()
//    part2(input).println()
}

fun Equation(line: String): Equation {
    val (expectedResult, numbers) = line.split(": ")
    return Equation(expectedResult.toLong(), numbers.split(" ").map { it.toLong() })
}

class Equation(val expectedResult: Long, private val numbers: List<Long>) {
    fun isValid(): Boolean {
        val operationCombos: List<List<Operation>> = operationCombos(numbers.lastIndex)
        for (operationCombination in operationCombos) {
            var index = 0
            val operation: (Long, Long) -> Long = { a, b ->
                when (operationCombination[index++]) {
                    Operation.plus -> a + b
                    Operation.times -> a * b
                }
            }
            // maybe: optimize to break out of reduce as soon as result > expectedResult
            if (numbers.reduce(operation) == expectedResult) return true
        }
        return false
    }

    private fun operationCombos(length: Int): List<List<Operation>> {
        return if (length == 0) {
            listOf(emptyList())
        } else {
            operationCombos(length - 1).flatMap { combo ->
                operations.map { op -> combo + op }
            }
        }
    }
}

enum class Operation {
    plus,
    times
}

object Operations {
    val operations = listOf(Operation.plus, Operation.times)
}
