fun main() {
    fun List<String>.parse(decryptionKey: Long = 1L) = map { it.toLong() * decryptionKey }

    fun List<Long>.mix(times: Int = 1): List<Long> {
        // using `IndexedValue<T>` to keep track of the original index (because the data can contain duplicates)
        val data = this.withIndex().toMutableList()

        repeat(times) {
            data.indices.forEach { originalIndex ->
                val i = data.indexOfFirst { (index) -> index == originalIndex }

                // first we remove item
                val item = data.removeAt(i)

                // re-add the item back at the new index
                data.add((i + item.value).mod(data.size), item)
            }
        }

        // strip out temporary data structure
        return data.map { it.value }
    }

    fun List<Long>.grooveCoordinates() = indexOf(0).let { zero ->
        listOf(1000, 2000, 3000).map { this[(zero + it) % size] }
    }

    fun part1(input: List<String>): Long {
        val parsed = input.parse()
        val mixed = parsed.mix()

        return mixed.grooveCoordinates().sum()
    }

    fun part2(input: List<String>): Long {
        val parsed = input.parse(811589153)
        val mixed = parsed.mix(10)

        return mixed.grooveCoordinates().sum()
    }

    val testInput = readInput("Day20_test")
    println(part1(testInput)) // 3
    println(part2(testInput)) // 1623178306

    val input = readInput("Day20")
    println(part1(input)) // 9866
    println(part2(input)) // 12374299815791
}
