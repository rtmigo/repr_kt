plugins {
    id("application")
    kotlin("jvm") version "1.6.10"
}

repositories { mavenCentral() }
application { mainClass.set("MainKt") }

dependencies {
    implementation("io.github.rtmigo:repr") { version { branch = "staging" } }
}