fun main() {
    fun findCommon(a: String, b: String): String {
        val set = a.toSet()

        return b.filter { set.contains(it) }
    }

    fun Char.toPriority() = if (this.isLowerCase()) this - 'a' + 1 else this - 'A' + 27

    fun part1(input: List<String>): Int {
        return input.sumOf {
            val firstHalf = it.take(it.length / 2)
            val secondHalf = it.takeLast(it.length / 2)

            findCommon(firstHalf, secondHalf)
                .first()
                .toPriority()
        }
    }

    fun part2(input: List<String>): Int {
        return input
            .chunked(3)
            .sumOf { group ->
                group
                    .reduce { acc, it -> findCommon(acc, it) }
                    .first()
                    .toPriority()
            }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
