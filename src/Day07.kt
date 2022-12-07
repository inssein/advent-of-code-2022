sealed class Node {
    abstract val name: String
    abstract val size: Int

    data class File(override val name: String, override val size: Int) : Node()

    data class Directory(
        override val name: String, 
        val parent: Directory?,
        val children: MutableList<Node> = mutableListOf()
    ) : Node() {

        override val size get() = children.fold(0) { acc, it -> acc + it.size }

        fun findDirectories(predicate: (Directory) -> Boolean): List<Directory> {
            val directories = children.filterIsInstance(Directory::class.java)

            return directories.filter(predicate) + directories.flatMap { it.findDirectories(predicate) }
        }

        override fun toString() = "Directory(name=$name, size=${size})"
    }

    companion object {
        fun build(input: List<String>): Directory {
            val root = Directory("/", null)

            input.fold(root) { cwd, line ->
                val parts = line.split(" ")

                when {
                    // change directory
                    parts.getOrNull(1) == "cd" -> when (parts[2]) {
                        "/" -> root
                        ".." -> cwd.parent ?: error("invalid state")
                        else -> cwd.findDirectories { it.name == parts[2] }.first()
                    }

                    // noop
                    parts.getOrNull(1) == "ls" -> cwd

                    // add directory
                    parts[0] == "dir" -> cwd.children.add(Directory(parts[1], cwd)).let { cwd }

                    // add file
                    else -> cwd.children.add(File(parts[1], parts[0].toInt())).let { cwd }

                }
            }

            return root
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return Node
            .build(input)
            .findDirectories { it.size <= 100_000 }
            .sumOf { it.size }
    }

    fun part2(input: List<String>): Int {
        val root = Node.build(input)
        val unused = 70_000_000 - root.size
        val minSpace = 30_000_000 - unused

        return root
            .findDirectories { it.size >= minSpace }
            .minBy { it.size }
            .size
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    println(part1(input)) // 1206825
    println(part2(input)) // 9608311
}
