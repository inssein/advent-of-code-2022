import kotlin.math.max

data class Valve(
    val name: String,
    val flowRate: Int,
    val tunnels: List<String>,
    val index: Int
)

data class Path(val time: Int, val valve: String, val bitmask: Int)

fun main() {
    fun List<String>.toValves() = this.mapIndexed { i, it ->
        val name = it.substringAfter("Valve ").take(2)
        val rate = it.substringAfter("=").substringBefore(";").toInt()
        val valves = it.split(", ").mapIndexed { ix, s ->
            if (ix == 0) s.takeLast(2) else s
        }

        Valve(name, rate, valves, i)
    }

    /**
     * Calculate the shortest paths using the floyd warshall algorithm.
     */
    fun shortestPaths(valves: Map<String, Valve>): Map<String, Map<String, Int>> {
        val shortestPaths = valves.mapValues { (_, v) ->
            v.tunnels.associateWith { 1 }.toMutableMap()
        }.toMutableMap()

        for (k in shortestPaths.keys) {
            for (i in shortestPaths.keys) {
                for (j in shortestPaths.keys) {
                    val ik = shortestPaths[i]?.get(k) ?: 99
                    val kj = shortestPaths[k]?.get(j) ?: 99
                    val ij = shortestPaths[i]?.get(j) ?: 99

                    if (ik + kj < ij) {
                        shortestPaths[i]?.set(j, ik + kj)
                    }
                }
            }
        }

        // filter out paths that have 0 flow rate
        return shortestPaths.mapValues { (_, v) ->
            v.filter { valves[it.key]?.flowRate != 0 }
        }
    }

    fun dfs(
        path: Path,
        cache: MutableMap<Path, Int>,
        valves: Map<String, Valve>,
        shortestPaths: Map<String, Map<String, Int>>,
    ): Int {
        val cached = cache[path]
        if (cached != null) {
            return cached
        }

        val paths = shortestPaths[path.valve] ?: error("invalid")
        var maxTime = 0

        paths.forEach { (k, d) ->
            val valve = valves[k] ?: error("invalid")
            val time = path.time - d - 1
            val bit = 1 shl valve.index

            if (path.bitmask.and(bit) == 0 && time > 0) {
                maxTime = max(
                    maxTime,
                    dfs(Path(time, k, path.bitmask.or(bit)), cache, valves, shortestPaths) + valve.flowRate * time
                )
            }
        }

        return maxTime.also { cache[path] = maxTime }
    }

    fun part1(input: List<String>): Int {
        val valves = input.toValves()
        val valveMap = valves.associateBy { it.name }
        val shortestPaths = shortestPaths(valveMap)

        return dfs(Path(30, "AA", 0), mutableMapOf(), valveMap, shortestPaths)
    }

    fun part2(input: List<String>): Int {
        val valves = input.toValves()
        val shortestPaths = shortestPaths(valves.associateBy { it.name })
        val optimalValves = valves.filter { it.flowRate > 0 }.mapIndexed { i, it -> it.copy(index = i) }
        val optimalValveMap = optimalValves.associateBy { it.name }
        val cache = mutableMapOf<Path, Int>()

        val bitmask = (1 shl optimalValves.size) - 1
        var maxTime = 0

        for (i in 0..(bitmask + 1) / 2) {
            val elf = dfs(Path(26, "AA", i), cache, optimalValveMap, shortestPaths)
            val elephant = dfs(Path(26, "AA", bitmask.xor(i)), cache, optimalValveMap, shortestPaths)

            maxTime = max(maxTime, elf + elephant)
        }

        return maxTime
    }

    val testInput = readInput("Day16_test")
    println(part1(testInput)) // 1651
    println(part2(testInput)) // 1707

    val input = readInput("Day16")
    println(part1(input)) // 1474
    println(part2(input)) // 2100
}
