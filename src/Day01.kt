import java.util.PriorityQueue

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

    fun part2(input: List<String>): Int {
        val top3 = input.fold(0 to PriorityQueue<Int>()) { (sum, acc), it ->
            if (it.isEmpty()) {
                acc.add(sum)

                if (acc.size > 3) {
                    acc.poll()
                }

                0 to acc
            } else {
                sum + it.toInt() to acc
            }
        }.second

        return top3.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    check(part2(input) == 200116)

    println(part1(input))
    println(part2(input))
}
