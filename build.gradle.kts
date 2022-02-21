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
    //api(kotlin("stdlib-jdk8"))

//
   // Align versions of all Kotlin components
    //implementation(platform(kotlin("bom")))    // Use the Kotlin JDK 8 standard library.
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
//
//    // Testing
    //testImplementation ("org.junit.jupiter:junit-jupiter-api")
    testImplementation(kotlin("test"))
    //testImplementation("org.jetbrains.kotlin:kotlin-test")
    //testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    //testImplementation(platform("org.junit:junit-bom:5.8.2"))
    //testImplementation("org.junit.jupiter:junit-jupiter")
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