fun main() {
    fun buildSignal(input: List<String>): List<Int> {
        return input.fold(listOf(1)) { acc, it ->
            val register = acc.last()

            when (it.take(4)) {
                "noop" -> acc.plus(register)
                "addx" -> acc.plus(listOf(register, register + it.substring(5).toInt()))
                else -> acc
            }
        }
    }

    fun part1(input: List<String>): Int {
        val signal = buildSignal(input)

        return (20..signal.size step 40).sumOf {
            it * signal[it - 1]
        }
    }

    fun part2(input: List<String>): String {
        return buildSignal(input)
            .mapIndexed { index, signal ->
                if (index % 40 in signal - 1..signal + 1) {
                    "#"
                } else {
                    "."
                }
            }
            .windowed(40, 40, false)
            .joinToString("\n") {
                it.joinToString("")
            }
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
    check(
        part2(testInput) == """
##..##..##..##..##..##..##..##..##..##..
###...###...###...###...###...###...###.
####....####....####....####....####....
#####.....#####.....#####.....#####.....
######......######......######......####
#######.......#######.......#######.....
    """.trimIndent()
    )

    val input = readInput("Day10")
    println(part1(input)) // 14160
    println(part2(input))
}
