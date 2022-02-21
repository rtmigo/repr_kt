![Generic badge](https://img.shields.io/badge/status-experimenatal-red.svg)
![Generic badge](https://img.shields.io/badge/CI_JVM-8-blue.svg)
![JaCoCo](https://raw.github.com/rtmigo/repr_kt/dev_updated_by_actions/.github/badges/jacoco.svg)

# [repr](https://github.com/rtmigo/repr_kt#readme) (draft)

Kotlin/JVM library.

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

