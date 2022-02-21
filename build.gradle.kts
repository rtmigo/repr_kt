plugins {
    kotlin("jvm") version "1.6.10"
    id("java-library")
    jacoco
    java
}

group = "io.github.rtmigo"
version = "0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
}


kotlin {
    sourceSets {
        val main by getting
        val test by getting
    }
}

// TESTS ///////////////////////////////////////////////////////////////////////////////////////////

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating JaCoCo report
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // JaCoCo report is always generated after tests run
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(false)
        csv.required.set(true)
    }
}