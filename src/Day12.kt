import java.util.PriorityQueue

data class Edge(val point: Pair<Int, Int>, val elevation: Int) : Comparable<Edge> {
    override fun compareTo(other: Edge): Int = elevation.compareTo(other.elevation)
}

data class Grid(val grid: List<List<Int>>) {
    private val height = grid.size
    private val width = grid.first().size

    fun shortestPath(
        start: Pair<Int, Int>,
        isValidNeighbour: (Int, Int) -> Boolean,
        isDestination: (Pair<Int, Int>) -> Boolean
    ): Int {
        val visited = mutableSetOf<Pair<Int, Int>>()
        val queue = PriorityQueue<Edge>()
            .apply { add(Edge(start, 0)) }

        while (queue.isNotEmpty()) {
            val (point, elevation) = queue.poll()

            if (!visited.add(point)) {
                continue
            }

            val neighbours = neighbours(point, isValidNeighbour)

            if (neighbours.any { isDestination(it) }) {
                return elevation + 1
            }

            queue.addAll(neighbours.map { Edge(it, elevation + 1) })
        }

        error("No valid path.")
    }

    private fun neighbours(point: Pair<Int, Int>, isValidNeighbour: (Int, Int) -> Boolean): List<Pair<Int, Int>> {
        val (x, y) = point

        return listOfNotNull(
            if (y == 0) null else x to y - 1, // up
            if (y == height - 1) null else x to y + 1, // down
            if (x == 0) null else x - 1 to y, // left
            if (x == width - 1) null else x + 1 to y // right
        ).filter { isValidNeighbour(value(point), value(it)) }
    }

    fun value(point: Pair<Int, Int>) = grid[point.second][point.first]
}

fun main() {
    fun List<String>.parse(): Triple<Grid, Pair<Int, Int>, Pair<Int, Int>> {
        var start: Pair<Int, Int>? = null
        var end: Pair<Int, Int>? = null

        val grid = this.foldIndexed(listOf<List<Int>>()) { y, acc, line ->
            val t = line.mapIndexed { x, it ->
                when (it) {
                    'S' -> 0.also { start = x to y }
                    'E' -> 25.also { end = x to y }
                    else -> it - 'a'
                }
            }

            acc.plusElement(t)
        }

        return Triple(Grid(grid), start ?: error("invalid"), end ?: error("invalid"))
    }

    fun part1(input: List<String>): Int {
        val (grid, start, end) = input.parse()

        return grid.shortestPath(start, { a, b -> b - a <= 1 }, { it == end })
    }

    fun part2(input: List<String>): Int {
        val (grid, _, end) = input.parse()

        return grid.shortestPath(end, { a, b -> a - b <= 1 }, { grid.value(it) == 0 })
    }

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12")
    println(part1(input)) // 472
    println(part2(input)) // 465
}
