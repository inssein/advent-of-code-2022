import kotlin.math.max
import kotlin.math.min

fun main() {
    fun List<String>.buildGrid() = this.flatMap { line ->
        line.split(" -> ")
            .map { it.split(",").map { p -> p.toInt() } }
            .windowed(2)
            .flatMap { (from, to) ->
                val (fromX, fromY) = from
                val (toX, toY) = to

                if (fromX == toX) {
                    (min(fromY, toY)..max(fromY, toY)).map { y -> Pair(fromX, y) }
                } else {
                    (min(fromX, toX)..max(fromX, toX)).map { x -> Pair(x, fromY) }
                }
            }
    }.toMutableSet()

    fun trickleDown(
        grid: MutableSet<Pair<Int, Int>>,
        maxDepth: Int,
        endCondition: (() -> Boolean)? = null
    ): Int {
        var sands = 0

        while (true) {
            var startX = 500

            if (endCondition?.invoke() == true) {
                return sands
            }

            for (y in 0..maxDepth) {
                if (grid.contains(startX to y)) {                   // there is something blocking
                    if (!grid.contains(startX - 1 to y)) {          // nothing exists on the left
                        startX--
                    } else if (!grid.contains(startX + 1 to y)) {   // nothing exists on the right
                        startX++
                    } else {                                        // blocked
                        grid.add(startX to y - 1)
                        sands++
                        break
                    }
                }

                if (y == maxDepth) {
                    endCondition?.let {
                        grid.add(startX to y - 1)
                        sands++
                    } ?: return sands
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val grid = input.buildGrid()

        return trickleDown(grid, grid.maxOf { it.second })
    }

    fun part2(input: List<String>): Int {
        val grid = input.buildGrid()

        return trickleDown(grid, grid.maxOf { it.second } + 2) {
            grid.contains(500 to 0)
        }
    }

    val testInput = readInput("Day14_test")
    println(part1(testInput)) // 24
    println(part2(testInput)) // 93

    val input = readInput("Day14")
    println(part1(input)) // 994
    println(part2(input)) // 26283
}
