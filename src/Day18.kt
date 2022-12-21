fun main() {
    fun List<String>.toCubes(): Set<Triple<Int, Int, Int>> = this.map {
        val parts = it.split(",").map { s -> s.toInt() }

        Triple(parts[0], parts[1], parts[2])
    }.toSet()

    fun Triple<Int, Int, Int>.toSides(): List<Triple<Int, Int, Int>> {
        val (x, y, z) = this

        return listOf(
            Triple(x + 1, y, z),
            Triple(x - 1, y, z),
            Triple(x, y + 1, z),
            Triple(x, y - 1, z),
            Triple(x, y, z + 1),
            Triple(x, y, z - 1),
        )
    }

    fun part1(input: List<String>): Int {
        val cubes = input.toCubes()

        return cubes.sumOf {
            it.toSides().count { s -> !cubes.contains(s) }
        }
    }

    fun part2(input: List<String>): Int {
        val cubes = input.toCubes()

        val xRange = cubes.minOf { it.first } - 1..cubes.maxOf { it.first } + 1
        val yRange = cubes.minOf { it.second } - 1..cubes.maxOf { it.second } + 1
        val zRange = cubes.minOf { it.third } - 1..cubes.maxOf { it.third } + 1

        val toVisit = mutableListOf(Triple(xRange.first, yRange.first, zRange.first))
        val visited = mutableSetOf<Triple<Int, Int, Int>>()
        var sides = 0

        while (toVisit.isNotEmpty()) {
            val point = toVisit.removeFirst()

            if (!visited.add(point)) {
                continue
            }

            val sidesInRange = point.toSides().filter { (x, y, z) -> x in xRange && y in yRange && z in zRange }
            val sidesOut = sidesInRange.filterNot { it in cubes }

            toVisit.addAll(sidesOut)
            sides += sidesInRange.size - sidesOut.size
        }

        return sides
    }

    val testInput = readInput("Day18_test")
    println(part1(testInput)) // 64
    println(part2(testInput)) // 58

    val input = readInput("Day18")
    println(part1(input)) // 3326
    println(part2(input)) // 1996
}
