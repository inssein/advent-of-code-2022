import kotlinx.serialization.json.*
import kotlinx.serialization.json.Json.Default.parseToJsonElement

sealed class Packet : Comparable<Packet> {
    data class Value(val value: Int) : Packet() {
        fun toList() = Container(listOf(this))

        override fun compareTo(other: Packet): Int = when (other) {
            is Value -> value.compareTo(other.value)
            is Container -> toList().compareTo(other)
        }
    }

    data class Container(val values: List<Packet>) : Packet() {
        override fun compareTo(other: Packet): Int = when (other) {
            is Value -> compareTo(other.toList())
            is Container -> values.zip(other.values)
                .map { (left, right) -> left.compareTo(right) }
                .firstOrNull { it != 0 } ?: values.size.compareTo(other.values.size)
        }
    }
}

fun main() {
    fun JsonElement.parse(): Packet = when (this) {
        is JsonArray -> Packet.Container(this.map { it.parse() })
        else -> Packet.Value(this.jsonPrimitive.int)
    }

    fun String.toPacket() = parseToJsonElement(this).parse()

    fun List<String>.toPackets() = this
        .chunked(3)
        .map { (left, right) -> left.toPacket() to right.toPacket() }

    fun part1(input: List<String>): Int {
        return input
            .toPackets()
            .foldIndexed(0) { i, acc, (left, right) ->
                if (left < right) {
                    acc + i + 1
                } else {
                    acc
                }
            }
    }

    fun part2(input: List<String>): Int {
        val dividerTop = Packet.Container(listOf(Packet.Container(listOf(Packet.Value(2)))))
        val dividerBottom = Packet.Container(listOf(Packet.Container(listOf(Packet.Value(6)))))

        val packets = input
            .toPackets()
            .flatMap { (left, right) -> listOf(left, right) }
            .plus(listOf(dividerTop, dividerBottom))
            .sorted()

        return (packets.indexOf(dividerTop) + 1) * (packets.indexOf(dividerBottom) + 1)
    }

    val testInput = readInput("Day13_test")
    println(part1(testInput)) // 13
    println(part2(testInput)) // 140

    val input = readInput("Day13")
    println(part1(input)) // 5675
    println(part2(input)) // 20383
}
