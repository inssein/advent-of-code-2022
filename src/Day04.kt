fun main() {
    fun String.toRange(): IntRange = this.split('-')
        .let { (first, second) -> first.toInt()..second.toInt() }

    fun String.toRanges(): Pair<IntRange, IntRange> = this.split(',')
        .let { (first, second) -> first.toRange() to second.toRange() }

    fun IntRange.fullyOverlaps(other: IntRange) = first <= other.first && last >= other.last

    fun IntRange.overlaps(other: IntRange) = first <= other.last && other.first <= last

    fun part1(input: List<String>): Int {
        return input.count {
            it.toRanges().let { (firstElf, secondElf) ->
                firstElf.fullyOverlaps(secondElf) || secondElf.fullyOverlaps(firstElf)
            }
        }
    }

    fun part2(input: List<String>): Int {
        return input.count {
            it.toRanges().let { (firstElf, secondElf) ->
                firstElf.overlaps(secondElf)
            }
        }
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
