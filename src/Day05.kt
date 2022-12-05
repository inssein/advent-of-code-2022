fun main() {
    fun Array<ArrayDeque<Char>>.topOfStack() = this.mapNotNull { it.firstOrNull() }.joinToString("")

    fun move(command: String, list: Array<ArrayDeque<Char>>, oneAtATime: Boolean = true) {
        // could use a regex like "move ([0-9]*) from ([0-9]*) to ([0-9]*)", but prefer split
        val parts = command.split(" ")

        val num = parts[1].toInt()
        val from = parts[3].toInt()
        val to = parts[5].toInt()

        val items = (1..num).map { list[from - 1].removeFirst() }
        list[to - 1].addAll(0, if (oneAtATime) items.reversed() else items)
    }

    fun buildStacks(input: List<String>, moveOneAtATime: Boolean = true): Array<ArrayDeque<Char>> {
        // cheat, based on the input we know it's up to a max of 10 stacks, so we pre-create them
        return input.fold(Array(10) { ArrayDeque() }) { stacks, line ->
            if (line.startsWith("move")) {
                // instruction
                move(line, stacks, moveOneAtATime)
            } else if (line.isEmpty() || line.startsWith(" 1")) {
                // ignore empty line or line with stack numbers
            } else {
                // stack definition, grab all letters
                for (i in 1..line.length step 4) {
                    val char = line[i]

                    if (char.isLetter()) {
                        stacks[i / 4].add(char)
                    }
                }
            }

            stacks
        }
    }

    fun part1(input: List<String>): String {
        return buildStacks(input, true).topOfStack()
    }

    fun part2(input: List<String>): String {
        return buildStacks(input, false).topOfStack()
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
