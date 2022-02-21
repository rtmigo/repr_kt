![Generic badge](https://img.shields.io/badge/status-experimenatal-red.svg)
![Generic badge](https://img.shields.io/badge/CI_JVM-8-blue.svg)
![JaCoCo](https://raw.github.com/rtmigo/repr_kt/dev_updated_by_actions/.github/badges/jacoco.svg)

# [repr](https://github.com/rtmigo/repr_kt#readme) (draft)

Kotlin/JVM library. Converts Kotlin objects and data structures to strings.

`.toRepr()` aims to produce a string that is valid Kotlin code. Ideally simple cases, 
this string can be copied into the project's source code to create the same data structure.

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

```
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

This library implements the `toRepr` function. Calling `data.toRepr()` would give us this:

```kotlin
mapOf(
    "week" to listOf(
        Day(isWeekend=true, name="Sunday", num=7), 
        Day(isWeekend=false, name="Monday", num=1), 
        Day(isWeekend=false, name="Tuesday", num=2), 
        Day(isWeekend=false, name="Wednesday", num=3), 
        Day(isWeekend=false, name="Thursday", num=4), 
        Day(isWeekend=false, name="Friday", num=5), 
        Day(isWeekend=true, name="Saturday", num=6)
    ), 
    "numFormat" to "ISO-8601"
)
```

*(both examples formatted for ease of reading)*

This library is inspired by Python's built-in 
[`repr`]([https://docs.python.org/3/library/functions.html#repr]) function (`__repr__`).

# Install

(if you use Gradle scripts in Groovy)

Edit **settings.gradle**:

```groovy
// add this:
sourceControl {
  gitRepository(URI.create("https://github.com/rtmigo/repr_kt.git")) {
    producesModule("io.github.rtmigo:repr")
  }
}
```

Edit **build.gradle**:

```groovy
dependencies {
    // add this: 
    implementation("io.github.rtmigo:repr") { version { branch = 'staging' } }
}    
```

<details>
  <summary>Or depend on particular version</summary>

Edit **build.gradle**:

```groovy
dependencies {
    // add this:     
    implementation "io.github.rtmigo:repr:0.0.1"
}
```

(the changes to **settings.gradle** are the same as above)
</details>

## Use

```kotlin
import io.github.rtmigo.repr.toRepr

fun main() {
    val x = listOf(1, 2, 3)
    println(x.toRepr())
}
```