import kotlin.math.abs

fun main() {
    data class Sensor(val position: Pair<Int, Int>, val distance: Int)
    data class Beacon(val position: Pair<Int, Int>, val sensors: MutableList<Sensor>)

    fun List<String>.toBeaconMap() = this.fold(mutableMapOf<Pair<Int, Int>, Beacon>()) { acc, it ->
        val parts = it.split("=")
        val sX = parts[1].substringBefore(",").toInt()
        val sY = parts[2].substringBefore(":").toInt()
        val bX = parts[3].substringBefore(",").toInt()
        val bY = parts[4].toInt()
        val beacon = bX to bY
        val sensor = Sensor(sX to sY, abs(sX - bX) + abs(sY - bY))

        if (acc.contains(beacon)) {
            acc[beacon]?.sensors?.add(sensor)
        } else {
            acc[beacon] = Beacon(beacon, mutableListOf(sensor))
        }

        acc
    }

    fun List<Sensor>.toCoverage(row: Int) = this.mapNotNull {
        val (sX, sY) = it.position

        // is in range?
        if (abs(row - sY) <= it.distance) {
            val distance = (it.distance - abs(row - sY))

            (sX - distance)..(sX + distance)
        } else {
            null
        }
    }

    fun part1(input: List<String>, row: Int): Int {
        val beaconMap = input.toBeaconMap()
        val sensors = beaconMap.values.flatMap { it.sensors }
        val ranges = sensors.toCoverage(row)

        // @note: this would not work if there was a gap in the range
        val minX = ranges.minOf { it.first }
        val maxX = ranges.maxOf { it.last }

        return maxX - minX
    }

    fun part2(input: List<String>, max: Int): Long {
        val beaconMap = input.toBeaconMap()
        val beacons = beaconMap.values
        val sensors = beacons.flatMap { it.sensors }

        for (y in 0..max) {
            var minX = 0
            val coverage = sensors.toCoverage(y).sortedBy { it.first }

            // find a gap in the range
            for (r in coverage) {
                if (minX - r.first < 0) {
                    val result = minX + (r.first - minX) / 2

                    return result * 4_000_000L + y
                }

                minX = minX.coerceAtLeast(r.last)
            }
        }

        return 0
    }

    val testInput = readInput("Day15_test")
    println(part1(testInput, 10)) // 26
    println(part2(testInput, 20)) // 56000011

    val input = readInput("Day15")
    println(part1(input, 2_000_000)) // 5073496
    println(part2(input, 4_000_000)) // 13081194638237
}
