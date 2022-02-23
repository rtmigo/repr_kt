import io.github.rtmigo.repr.toRepr

class TimeDefault(days: Int) {
    val hours = days * 24
}

class TimeTweaked(days: Int) {
    val hours = days * 24
    fun toRepr() = """TimeTweaked(days=${hours / 24})"""
}

fun main() {
    println(listOf(TimeDefault(days = 1), TimeDefault(days = 7)).toRepr())
    println(listOf(TimeTweaked(days = 1), TimeTweaked(days = 7)).toRepr())
}