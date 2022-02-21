![Generic badge](https://img.shields.io/badge/status-experimental-red.svg)
![Generic badge](https://img.shields.io/badge/CI_JVM-8-blue.svg)
![JaCoCo](https://raw.github.com/rtmigo/repr_kt/dev_updated_by_actions/.github/badges/jacoco.svg)

# [repr](https://github.com/rtmigo/repr_kt#readme)

Kotlin/JVM library. Converts Kotlin objects to strings.

`.toRepr()` aims to produce a string that is valid Kotlin code. Ideally, 
this string can be copied into the project's source code to create the same objects.

Let's say we have the following data:

```kotlin
data class Day(val name: String, 
               val num: Int, 
               val isWeekend: Boolean)

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
```

Calling the default `data.toString()` would give us this:

```text
{
    week=[
        Day(name=Sunday, num=7, isWeekend=true), 
        Day(name=Monday, num=1, isWeekend=false), 
        Day(name=Tuesday, num=2, isWeekend=false), 
        Day(name=Wednesday, num=3, isWeekend=false), 
        Day(name=Thursday, num=4, isWeekend=false), 
        Day(name=Friday, num=5, isWeekend=false), 
        Day(name=Saturday, num=6, isWeekend=true)
    ], 
    numFormat=ISO-8601
}
```

Calling `data.toRepr()` (defined in this library) would give us a string, that is a correct 
Kotlin code:

```kotlin
mapOf(
    "week" to listOf(
        Day(name="Sunday", num=7, isWeekend=true), 
        Day(name="Monday", num=1, isWeekend=false), 
        Day(name="Tuesday", num=2, isWeekend=false), 
        Day(name="Wednesday", num=3, isWeekend=false), 
        Day(name="Thursday", num=4, isWeekend=false), 
        Day(name="Friday", num=5, isWeekend=false), 
        Day(name="Saturday", num=6, isWeekend=true)
    ), 
    "numFormat" to "ISO-8601"
)
```

*(both examples formatted for ease of reading)*

The library uses the features of Kotlin reflection. It is inspired by Python's built-in 
[`repr`]([https://docs.python.org/3/library/functions.html#repr]) function (and `__repr__` 
overloads). 

# Install

*(instructions for Gradle Kotlin DSL)*

### settings.gradle.kts

```kotlin
sourceControl {
    gitRepository(java.net.URI("https://github.com/rtmigo/repr_kt.git")) {
        producesModule("io.github.rtmigo:repr")
    }
}
```

### build.gradle.kts

```kotlin
implementation("io.github.rtmigo:repr")  // newest version
```

or

```kotlin
implementation("io.github.rtmigo:repr:0.1.0")  // version 0.1.0
```

# Use

```kotlin
import io.github.rtmigo.repr.toRepr

fun main() {
    val data = listOf(1, 2, 3)
    println(data.toRepr())
}
```

Output:

```kotlin
listOf(1, 2, 3)
```

### Override .toRepr for your class

```kotlin
import io.github.rtmigo.repr.toRepr

class MyClass(val x: String) {
    fun toRepr() = """MyClass("${x.uppercase()}!!!")"""
}

fun main() {
    val data = listOf(MyClass("ping"), MyClass("pong"))
    println(data.toRepr())
}
```

Output:
```kotlin
listOf(MyClass("PING!!!"), MyClass("PONG!!!"))
```

Specifying the `toRepr` method here is similar to overloading the `__repr__()` method in Python.


# License

Copyright © 2022 Artёm IG <github.com/rtmigo>

Licensed under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
