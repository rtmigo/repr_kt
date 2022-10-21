plugins {
    kotlin("jvm") version "1.6.10"
    id("java-library")
    jacoco
    java
    id("maven-publish")
}
java {
    withSourcesJar()
    withJavadocJar()
}

group = "io.github.rtmigo"
version = "0.0.10+3"

publishing {
    publications {
        create<MavenPublication>("repr") {
            from(components["java"])
            pom {
                val github = "https://github.com/rtmigo/repr_kt"

                name.set("repr")
                description.set("Converts Kotlin objects to strings. Inspired by Python `repr`.")
                url.set(github)

                developers {
                    developer {
                        name.set("Artsiom iG")
                        email.set("ortemeo@gmail.com")
                    }
                }
                scm {
                    url.set(github)
                    connection.set(github.replace("https:", "scm:git:")) // + ".git"
                }
                licenses {
                    license {
                        name.set("Apache 2.0 License")
                        url.set("$github/blob/HEAD/LICENSE")
                    }
                }
            }
        }
    }
}

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

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating JaCoCo report
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // JaCoCo report is always generated after tests run
}

tasks.register("updateReadmeVersion") {
    doFirst {
        // найдем что-то вроде "io.github.rtmigo:repr:0.0.1"
        // и поменяем на актуальную версию
        val readmeFile = project.rootDir.resolve("README.md")
        val prefixToFind = "io.github.rtmigo:repr:"
        val regex = """(?<=${Regex.escape(prefixToFind)})[0-9\.+]+""".toRegex()
        val oldText = readmeFile.readText()
        val newText = regex.replace(oldText, project.version.toString())
        if (newText!=oldText)
            readmeFile.writeText(newText)
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

tasks.register<Jar>("uberJar") {
    archiveClassifier.set("uber")
    duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.INCLUDE

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
             configurations.runtimeClasspath.get()
                 .filter { it.name.endsWith("jar") }
                 .map { zipTree(it) }
         })
}

