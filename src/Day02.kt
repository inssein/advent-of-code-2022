enum class Hand(val score: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    fun winScore() = (score % 3) + 1
    fun loseScore() = if (score == 1) 3 else score - 1

    companion object {
        fun fromChar(c: Char) = when (c) {
            'A', 'X' -> ROCK
            'B', 'Y' -> PAPER
            'C', 'Z' -> SCISSORS
            else -> error("Invalid")
        }
    }
}

fun Char.toHand() = Hand.fromChar(this)

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

            when (it.last()) {
                'X' -> request.loseScore()
                'Y' -> 3 + request.score
                'Z' -> 6 + request.winScore()
                else -> error("Invalid")
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
