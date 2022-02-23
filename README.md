![Generic badge](https://img.shields.io/badge/status-experimental-red.svg)
![Generic badge](https://img.shields.io/badge/JVM-8-blue.svg)
![JaCoCo](https://raw.github.com/rtmigo/repr_kt/dev_updated_by_actions/.github/badges/jacoco.svg)

# io.github.rtmigo : [repr](https://github.com/rtmigo/repr_kt#readme)

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

## Override .toRepr for your class

`.toRepr()` automatically converts all objects. But sometimes you may want to tweak the way the 
are converted.

```kotlin
import kotlin.math.roundToInt

/** gets Double as input, but exposes only rounded Int */
class Quantity(fraction: Double) {
    val pct: Int = (fraction * 100).roundToInt()
}
```

The only public property of this object is `pct`. By default, the object will be converted to 
`Quantity(pct=...)`.

If you want the `fraction` value to be in the string, define the `Quantity.toRepr()` method.  


```kotlin
import io.github.rtmigo.repr.toRepr
import kotlin.math.roundToInt

class Quantity(fraction: Double) {
    val pct: Int = (fraction * 100).roundToInt()
    fun toRepr(): String = """Quantity(fraction=${fraction})"""
}

fun main() {
    val data = listOf(Quantity(0.17), Quantity(0.231))
    println(data.toRepr())
}
```

Output:
```kotlin
listOf(Quantity(fraction=0.17), Quantity(fraction=0.231))
```

Specifying the `toRepr` method here is similar to overloading the `__repr__()` method in Python.


# License

Copyright © 2022 Artёm IG <github.com/rtmigo>

Licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt).
