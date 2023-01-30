sealed class Monkey21 {
    abstract val name: String
    abstract fun yell(): Long

    data class NumberMonkey(override val name: String, val number: Long) : Monkey21() {
        override fun yell(): Long = number
    }

    data class MathMonkey(
        override val name: String,
        val leftName: String,
        val rightName: String,
        val op: Char,
        val finder: (String) -> Monkey21
    ) : Monkey21() {
        val left: Monkey21 by lazy { finder(leftName) }
        val right: Monkey21 by lazy { finder(rightName) }

        override fun yell(): Long {
            val lv = left.yell()
            val rv = right.yell()

            return when (op) {
                '+' -> lv + rv
                '-' -> lv - rv
                '*' -> lv * rv
                else -> lv / rv
            }
        }
    }
}

fun main() {
    fun List<String>.parse(): Map<String, Monkey21> {
        val r = mutableMapOf<String, Monkey21>()
        val finder = { name: String -> r[name] ?: throw Error("Monkey $name not defined") }

        forEach {
            val parts = it.split(": ")
            val name = parts[0]
            val mathParts = parts[1].split(" ")

            if (mathParts.size == 1) {
                r[name] = Monkey21.NumberMonkey(name, parts[1].toLong())
            } else {
                r[name] = Monkey21.MathMonkey(name, mathParts[0], mathParts[2], mathParts[1].first(), finder)
            }
        }

        return r
    }

    fun Map<String, Monkey21>.findRoot() = this["root"] ?: throw Error("Root monkey must be defined.")

    fun Monkey21.humanPath(): Set<Monkey21> = when (this) {
        is Monkey21.NumberMonkey -> if (name == "humn") setOf(this) else emptySet()
        is Monkey21.MathMonkey -> {
            val lPath = left.humanPath()
            val rPath = right.humanPath()

            when {
                lPath.isNotEmpty() -> lPath + this
                rPath.isNotEmpty() -> rPath + this
                else -> emptySet()
            }
        }
    }

    fun Monkey21.humanValue(humanPath: Set<Monkey21>, lv: Long): Long = when (this) {
        is Monkey21.NumberMonkey -> if (name == "humn") lv else number
        is Monkey21.MathMonkey -> {
            val lIsHuman = left in humanPath
            val rv = if (lIsHuman) right.yell() else left.yell()

            val v = when (op) {
                '+' -> lv - rv
                '-' -> if (lIsHuman) lv + rv else rv - lv
                '*' -> lv / rv
                else -> lv * rv
            }

            if (lIsHuman) left.humanValue(humanPath, v) else right.humanValue(humanPath, v)
        }
    }

    fun part1(input: List<String>) = input.parse().findRoot().yell()

    fun part2(input: List<String>): Long {
        val root = input.parse().findRoot() as Monkey21.MathMonkey
        val humanPath = root.humanPath()

        return if (root.left in humanPath) {
            root.left.humanValue(humanPath, root.right.yell())
        } else {
            root.right.humanValue(humanPath, root.left.yell())
        }
    }

    val testInput = readInput("Day21_test")
    println(part1(testInput)) // 152
    println(part2(testInput)) // 301

    val input = readInput("Day21")
    println(part1(input)) // 268597611536314
    println(part2(input)) // 3451534022348
}
