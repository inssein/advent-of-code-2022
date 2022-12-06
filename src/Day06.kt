fun main() {
    fun naive(input: String, capacity: Int): Int {
        return input.windowedSequence(capacity).indexOfFirst {
            it.toSet().size == capacity
        } + capacity
    }

    fun trackDuplicate(input: String, capacity: Int): Int {
        val duplicateMap = IntArray(26)
        var lastDuplicateIndex = 0

        for (i in input.indices) {
            val key = input[i] - 'a'
            val lastSeenIndex = duplicateMap[key]

            // track the last time we saw a duplicate
            if (lastDuplicateIndex < lastSeenIndex) {
                lastDuplicateIndex = lastSeenIndex
            }

            // end condition
            if (i - lastDuplicateIndex >= capacity) {
                return i + 1
            }

            // track duplicate
            duplicateMap[key] = i
        }

        return -1
    }

    fun part1(input: String): Int {
        return trackDuplicate(input, 4)
    }

    fun part2(input: String): Int {
        return trackDuplicate(input, 14)
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput[0]) == 7)
    check(part1(testInput[1]) == 5)
    check(part1(testInput[2]) == 6)
    check(part1(testInput[3]) == 10)
    check(part1(testInput[4]) == 11)

    check(part2(testInput[0]) == 19)
    check(part2(testInput[1]) == 23)
    check(part2(testInput[2]) == 23)
    check(part2(testInput[3]) == 29)
    check(part2(testInput[4]) == 26)

    val input = readInput("Day06").first()
    println(part1(input))
    println(part2(input))
}
