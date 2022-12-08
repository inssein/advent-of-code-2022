fun main() {
    fun List<String>.toGrid(): List<List<Int>> = this.map { s ->
        s.map { it.digitToInt() }
    }

    fun List<List<Int>>.toView(row: Int, col: Int): List<List<Int>> = listOf(
        (row - 1 downTo 0).map { this[it][col] }, // up
        (row + 1 until this.size).map { this[it][col] }, // down
        (col - 1 downTo 0).map { this[row][it] }, // left
        this[row].drop(col + 1), // right
    )

    fun List<List<Int>>.isVisible(row: Int, col: Int): Boolean {
        if (row == 0 || col == 0 || row == this.size - 1 || col == this.first().size - 1) {
            return true
        }

        return this.toView(row, col).any { direction ->
            direction.all { it < this[row][col] }
        }
    }

    fun List<List<Int>>.scenicScore(row: Int, col: Int): Int {
        if (row == 0 || col == 0 || row == this.size - 1 || col == this.first().size - 1) {
            return 0
        }

        return this.toView(row, col).map { direction ->
            direction
                .indexOfFirst { it >= this[row][col] }
                .let { if (it == -1) direction.size else it + 1 }
        }.reduce { acc, it -> acc * it }
    }

    fun part1(input: List<String>): Int {
        val grid = input.toGrid()

        return (grid.indices).sumOf { row ->
            (grid.first().indices).count { col ->
                grid.isVisible(row, col)
            }
        }
    }

    fun part2(input: List<String>): Int {
        val grid = input.toGrid()

        return (grid.indices).maxOf { row ->
            (grid.first().indices).maxOf { col ->
                grid.scenicScore(row, col)
            }
        }
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input)) // 1849
    println(part2(input)) // 201600
}
