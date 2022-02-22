plugins {
    kotlin("jvm") version "1.6.10"
    id("java-library")
    jacoco
    java
}

group = "io.github.rtmigo"
version = "0.0.8"

//Paths("./.README.template.md").re

tasks.register("pkgver") {
    doLast {
        println(project.version.toString())
    }
}

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

tasks.register("updateReadmeVersion") {
    doFirst {
        replateVersionInReadme()
    }
}

tasks.build {
    dependsOn("updateReadmeVersion")
}


tasks.jacocoTestReport {
    reports {
        xml.required.set(false)
        csv.required.set(true)
    }
}

/////////////////////////

fun replateVersionInReadme() {
    val srcFile = project.rootDir.resolve(".README.template.md")
    val dstFile = project.rootDir.resolve("README.md")

    srcFile.inputStream().use { input ->
        dstFile.printWriter().use { output ->
            input.bufferedReader().forEachLine { srcLine ->
                if (!srcLine.startsWith("!!! ")) {
                        val dstLine = srcLine.replace(
                            "__TEMPLATE_VERSION__",
                            project.version.toString()
                        )
                        output.println(dstLine)
                    }
            }
        }
    }
    //inputStream.close()
}

