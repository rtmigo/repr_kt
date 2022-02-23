import io.github.rtmigo.repr.toRepr

fun main() {

    data class Day(val name: String, val num: Int, val isWeekend: Boolean)

    val data = mapOf(
        "week" to listOf(
            Day("Sunday", 7, true),
            Day("Monday", 1, false),
            Day("Tuesday", 2, false),
            Day("Wednesday", 3, false),
            Day("Thursday", 4, false),
            Day("Friday", 5, false),
            Day("Saturday", 6, true)
        ),
        "numFormat" to "ISO-8601"
    )

    println("toString:")
    println(data.toString())

    println()
    println("toRepr:")
    println(data.toRepr())
}