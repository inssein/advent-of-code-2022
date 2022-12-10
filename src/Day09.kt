import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {
    data class Position(val x: Int, val y: Int) {
        fun move(direction: Char): Position {
            return when (direction) {
                'U' -> copy(y = y - 1)
                'D' -> copy(y = y + 1)
                'L' -> copy(x = x - 1)
                'R' -> copy(x = x + 1)
                else -> error("Invalid move.")
            }
        }

        fun moveTowardsHead(head: Position): Position {
            val xDelta = head.x - x
            val yDelta = head.y - y

            if (xDelta.absoluteValue <= 1 && yDelta.absoluteValue <= 1) {
                return this
            }

            return Position(xDelta.sign + x, yDelta.sign + y)
        }
    }

    fun countTailPositions(input: List<String>, knots: Int): Int {
        val visited = mutableSetOf<Position>()
        val nodes = Array(knots) { Position(0, 0) }

        input.forEach { line ->
            val direction = line[0]
            val moves = line.substring(2).toInt()

            repeat(moves) {
                nodes[knots - 1] = nodes[knots - 1].move(direction)

                for (j in knots - 2 downTo 0) {
                    nodes[j] = nodes[j].moveTowardsHead(nodes[j + 1])
                }

                visited.add(nodes[0])
            }
        }

        return visited.count()
    }

    fun part1(input: List<String>): Int {
        return countTailPositions(input, 2)
    }

    fun part2(input: List<String>): Int {
        return countTailPositions(input, 10)
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)

    val input = readInput("Day09")
    println(part1(input)) // 6030
    println(part2(input)) // 2545
}
