import kotlin.math.max

fun main() {
    fun List<String>.toJetDeltas() = this.first().map { if (it == '>') 1 else -1 }

    val rocks = listOf(
        listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(3, 0)),
        listOf(Pair(1, 0), Pair(0, 1), Pair(1, 1), Pair(2, 1), Pair(1, 2)),
        listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(2, 1), Pair(2, 2)),
        listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(0, 3)),
        listOf(Pair(0, 0), Pair(0, 1), Pair(1, 0), Pair(1, 1))
    )

    fun List<Pair<Int, Int>>.canPushTo(toX: Int, toY: Int, chamber: Set<Pair<Int, Int>>) =
        this.map { (x, y) -> x + toX to y + toY }.all { it.first in 0..6 && !chamber.contains(it) }

    fun List<Pair<Int, Int>>.canDropDown(toX: Int, toY: Int, dropped: Set<Pair<Int, Int>>) =
        toY > 1 && this.map { (x, y) -> x + toX to y + toY - 1 }.none { dropped.contains(it) }

    fun drop(jetDeltas: List<Int>, numRocks: Long): Long {
        var floor = 0
        var jetOffset = 0
        val cache = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
        val chamber = mutableSetOf<Pair<Int, Int>>()

        for (i in 0 until numRocks) {
            val rockIndex = (i % 5).toInt()
            val rock = rocks[rockIndex]
            var rockX = 2
            var rockY = floor + 4

            while (true) {
                val jetIndex = jetOffset++ % jetDeltas.size
                val cached = cache[rockIndex to jetIndex]

                if (cached != null) {
                    val (prevRockNum, elevation) = cached
                    val period = i - prevRockNum

                    // @note: not sure if this is the best way to detect a cycle
                    if (i > jetDeltas.size * 3 && i % period == numRocks % period) {
                        val cycleHeight = floor - elevation
                        val rocksRemaining = numRocks - i
                        val cyclesRemaining = (rocksRemaining / period) + 1
                        return elevation + (cycleHeight * cyclesRemaining)
                    }
                } else {
                    cache[rockIndex to jetIndex] = i.toInt() to floor
                }

                val pushTo = rockX + jetDeltas[jetIndex]

                if (rock.canPushTo(pushTo, rockY, chamber)) {
                    rockX = pushTo
                }

                if (rock.canDropDown(rockX, rockY, chamber)) {
                    rockY--
                } else {
                    val toAdd = rock.map { (x, y) -> x + rockX to y + rockY }
                    floor = max(floor, toAdd.maxOf { (_, y) -> y })
                    chamber.addAll(toAdd)

                    break
                }
            }
        }

        return floor.toLong()
    }

    fun part1(input: List<String>): Long {
        return drop(input.toJetDeltas(), 2022)
    }

    fun part2(input: List<String>): Long {
        return drop(input.toJetDeltas(), 1_000_000_000_000)
    }

    val testInput = readInput("Day17_test")
    println(part1(testInput)) // 3068
    println(part2(testInput)) // 1514285714288

    val input = readInput("Day17")
    println(part1(input)) // 3092
    println(part2(input)) // 1528323699442
}
