fun main() {
    fun part1(input: List<String>): Int {
        var max = 0
        var current = 0

        input.forEach {
            if (it.isEmpty()) {
                if (current > max) {
                    max = current
                }

                current = 0
            } else {
                current += it.toInt()
            }
        }

        return max
    }

    /**
     * Could optimize this implementation by creating a data structure that only holds the top 3 numbers, which would
     * void the need to sort the entire array.
     */
    fun part2(input: List<String>): Int {
        var current = 0

        val food = input.fold(listOf<Int>()) { acc, it ->
            if (it.isEmpty()) {
                acc
                    .plus(current)
                    .also { current = 0 }
            } else {
                current += it.toInt()
                acc
            }
        }

        return food
            .sortedDescending()
            .take(3)
            .fold(0) { acc, it -> acc + it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
