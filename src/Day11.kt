data class Operation(private val input: String) {
    private val suffix = input.substringAfter("new = old ")
    private val operator = suffix[0]
    private val rawAmount = suffix.substring(2)

    operator fun invoke(value: Long): Long {
        val amount = rawAmount.let {
            if (it == "old") value else it.toLong()
        }

        return when (operator) {
            '+' -> value + amount
            '*' -> value * amount
            else -> error("Invalid operator")
        }
    }
}

data class Monkey(
    val items: MutableList<Long>,
    val operation: Operation,
    val test: (Long) -> Int,
    val denominator: Int,
) {
    var inspections: Long = 0

    companion object {
        fun from(input: List<String>): Monkey {
            val items = input[1].substringAfter("  Starting items: ").split(", ").map { it.toLong() }
            val operation = Operation(input[2].substringAfter("  Operation: "))
            val denominator = input[3].substringAfter("  Test: divisible by ").toInt()
            val ifTrue = input[4].substringAfter("    If true: throw to monkey ").toInt()
            val ifFalse = input[5].substringAfter("    If false: throw to monkey ").toInt()
            val test: (Long) -> Int = { if (it % denominator == 0L) ifTrue else ifFalse }

            return Monkey(items.toMutableList(), operation, test, denominator)
        }
    }
}

fun main() {
    fun List<String>.buildMonkeys() =
        this.chunked(7).fold(listOf<Monkey>()) { acc, it -> acc.plus(Monkey.from(it)) }

    fun List<Monkey>.toBusiness() =
        this.map { it.inspections }.sortedDescending().take(2).reduce { it, acc -> it * acc }

    fun List<Monkey>.round(decompressor: (Long) -> Long) {
        this.forEach { monkey ->
            monkey.items.forEach { item ->
                val worry = decompressor(monkey.operation(item))

                this[monkey.test(worry)].items.add(worry)

                monkey.inspections++
            }

            monkey.items.clear()
        }
    }

    fun part1(input: List<String>): Long {
        val monkeys = input.buildMonkeys()

        repeat(20) {
            monkeys.round { it / 3 }
        }

        return monkeys.toBusiness()
    }

    fun part2(input: List<String>): Long {
        val monkeys = input.buildMonkeys()
        val stuff = monkeys.map { it.denominator }.reduce { acc, it -> acc * it }

        repeat(10000) {
            monkeys.round { it % stuff }
        }

        return monkeys.toBusiness()
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158L)

    val input = readInput("Day11")
    println(part1(input)) // 69918
    println(part2(input)) // 19573408701
}
