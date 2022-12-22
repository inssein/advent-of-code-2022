import java.util.PriorityQueue
import kotlin.math.ceil

fun main() {
    data class Cost(val ore: Int, val clay: Int, val obsidian: Int)

    data class Blueprint(
        val id: Int,
        val oreRobotCost: Cost,
        val clayRobotCost: Cost,
        val obsidianRobotCost: Cost,
        val geodeRobotCost: Cost
    ) {
        private val costs = listOf(oreRobotCost, clayRobotCost, obsidianRobotCost, geodeRobotCost)
        val maxOre = costs.maxOf { it.ore }
        val maxClay = costs.maxOf { it.clay }
        val maxObsidian = costs.maxOf { it.obsidian }
    }

    data class FactoryState(
        val time: Int = 1,
        val ore: Int = 1,
        val oreRobots: Int = 1,
        val clay: Int = 0,
        val clayRobots: Int = 0,
        val obsidian: Int = 0,
        val obsidianRobots: Int = 0,
        val geodes: Int = 0,
        val geodeRobots: Int = 0
    ) : Comparable<FactoryState> {
        fun addCost(cost: Cost): FactoryState {
            val timeToAdd = maxOf(
                if (cost.ore <= ore) 0 else ceil((cost.ore - ore) / oreRobots.toFloat()).toInt(),
                if (cost.clay <= clay) 0 else ceil((cost.clay - clay) / clayRobots.toFloat()).toInt(),
                if (cost.obsidian <= obsidian) 0 else ceil((cost.obsidian - obsidian) / obsidianRobots.toFloat()).toInt()
            ) + 1

            return copy(
                time = time + timeToAdd,
                ore = (ore - cost.ore) + (timeToAdd * oreRobots),
                clay = (clay - cost.clay) + (timeToAdd * clayRobots),
                obsidian = (obsidian - cost.obsidian) + (timeToAdd * obsidianRobots),
                geodes = geodes + (timeToAdd * geodeRobots)
            )
        }

        fun buildNextStates(blueprint: Blueprint, budget: Int) = buildList {
            if (blueprint.maxOre > oreRobots && ore > 0) {
                add(addCost(blueprint.oreRobotCost).copy(oreRobots = oreRobots + 1))
            }

            if (blueprint.maxClay > clayRobots && ore > 0) {
                add(addCost(blueprint.clayRobotCost).copy(clayRobots = clayRobots + 1))
            }

            if (blueprint.maxObsidian > obsidianRobots && ore > 0 && clay > 0) {
                add(addCost(blueprint.obsidianRobotCost).copy(obsidianRobots = obsidianRobots + 1))
            }

            if (ore > 0 && obsidian > 0) {
                add(addCost(blueprint.geodeRobotCost).copy(geodeRobots = geodeRobots + 1))
            }
        }.filter { it.time <= budget }

        fun maxGeodes(budget: Int): Int {
            val remainder = budget - time

            // n(n+1)/2 for summation
            val capacity = (remainder) * (remainder + 1) / 2 + geodeRobots * remainder

            return geodes + capacity
        }

        override fun compareTo(other: FactoryState) = other.geodes.compareTo(geodes)
    }

    fun List<String>.toBlueprints() = this.mapIndexed { i, it ->
        val parts = it.split(" ")

        Blueprint(
            i + 1,
            Cost(parts[6].toInt(), 0, 0),
            Cost(parts[12].toInt(), 0, 0),
            Cost(parts[18].toInt(), parts[21].toInt(), 0),
            Cost(parts[27].toInt(), 0, parts[30].toInt()),
        )
    }

    fun maxGeodes(blueprint: Blueprint, budget: Int): Int {
        var geodes = 0
        val queue = PriorityQueue<FactoryState>().apply { add(FactoryState()) }

        while (queue.isNotEmpty()) {
            val state = queue.poll()

            if (state.time < budget && state.maxGeodes(budget) > geodes) {
                queue.addAll(state.buildNextStates(blueprint, budget))
            }

            geodes = maxOf(geodes, state.geodes)
        }

        return geodes
    }

    fun part1(input: List<String>): Int {
        return input
            .toBlueprints()
            .sumOf { it.id * maxGeodes(it, 24) }
    }

    fun part2(input: List<String>): Int {
        return input
            .toBlueprints()
            .take(3)
            .map { maxGeodes(it, 32) }
            .reduce { acc, it -> acc * it }
    }

    val testInput = readInput("Day19_test")
    println(part1(testInput)) // 33
    println(part2(testInput)) // 3472

    val input = readInput("Day19")
    println(part1(input)) // 1681
    println(part2(input)) // 1996
}
