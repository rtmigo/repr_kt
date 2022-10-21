![Generic badge](https://img.shields.io/badge/JVM-8-blue.svg)
![JaCoCo](https://raw.github.com/rtmigo/repr_kt/dev_updated_by_actions/.github/badges/jacoco.svg)

# [repr](https://github.com/rtmigo/repr_kt#readme)

Kotlin/JVM library. Converts Kotlin objects to strings.

`.toRepr()` is like `.toString()`, but aims to produce a valid Kotlin code.

Let's say we have the following:

```kotlin
data class Planet(val name: String, 
                  val diameter: Int)

val data = mapOf(
    "planets" to listOf(
        Planet("Venus", 12104),
        Planet("Earth", 12742),
        Planet("Mars", 6779)    
    ),
    "location" to "Solar System"
)
```

Calling `data.toString()` would give us this:

```text
{
    planets=[
        Planet(name=Venus, diameter=12104), 
        Planet(name=Earth, diameter=12742),
        Planet(name=Mars, diameter=6779)      
    ], 
    location=Solar System
}
```

Calling `data.toRepr()`  would give us this:

```kotlin
mapOf(
    "planets" to listOf(
        Planet(name="Venus", diameter=12104),
        Planet(name="Earth", diameter=12742),
        Planet(name="Mars", diameter=6779)
    ), 
    "location" to "Solar System"
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
implementation("io.github.rtmigo:repr:0.0.9")  // particular version
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

## Customize .toRepr for your class

The `.toRepr()` extension method automatically converts `Any` objects.

Sometimes you may want to customize the way how your objects are converted. To do this,
define `toRepr(): String` method for your class.

```kotlin
import io.github.rtmigo.repr.toRepr

class TimeDefault(days: Int) {
    val hours = days * 24
}

class TimeTweaked(days: Int) {
    val hours = days * 24
    fun toRepr() = "TimeTweaked(days=${hours / 24})"
}

fun main() {
    println(listOf(TimeDefault(days = 1), TimeDefault(days = 7)).toRepr())
    println(listOf(TimeTweaked(days = 1), TimeTweaked(days = 7)).toRepr())
}
```

Output:

```text
listOf(TimeDefault(hours=24), TimeDefault(hours=168))
listOf(TimeTweaked(days=1), TimeTweaked(days=7))
```
<sub>Specifying the `toRepr` method here is similar to overloading the `__repr__()` method in Python.</sub>


# License

Copyright © 2022 [Artsiom iG](https://github.com/rtmigo).
Licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt).
