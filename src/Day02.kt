enum class Hand(val score: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);
}

fun Char.toHand() = when (this) {
    'A', 'X' -> Hand.ROCK
    'B', 'Y' -> Hand.PAPER
    'C', 'Z' -> Hand.SCISSORS
    else -> error("Invalid hand: $this")
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            val request = it.first().toHand()
            val response = it.last().toHand()

            response.score + when (request) {
                response -> 3
                Hand.ROCK -> if (response == Hand.PAPER) 6 else 0
                Hand.PAPER -> if (response == Hand.SCISSORS) 6 else 0
                Hand.SCISSORS -> if (response == Hand.ROCK) 6 else 0
            }
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            val request = it.first().toHand()

            when (val outcome = it.last()) {
                'X' -> if (request.score == 1) 3 else request.score - 1
                'Y' -> 3 + request.score
                'Z' -> 6 + (request.score % 3) + 1
                else -> error("Invalid outcome: $outcome")
            }
        }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
