import json
import os.path
import subprocess
from tempground import TempGround


def test_package(maven_url: str, ver: str):
    with TempGround(
            files={
                # minimalistic build script to use the library
                "build.gradle.kts": """
                    plugins {
                        id("application")
                        kotlin("jvm") version "1.6.20"
                    }
    
                    repositories { 
                        maven { url = uri("__TEMP_REPO__") }
                        mavenCentral() 
                    }
                    
                    application { mainClass.set("MainKt") }
    
                    dependencies {
                        implementation("io.github.rtmigo:repr:__VERSION__")
                    }
                """.replace("__VERSION__", ver)
                        .replace("__TEMP_REPO__", maven_url),

                # kotlin code that imports and uses the library
                "src/main/kotlin/Main.kt": """
                    import io.github.rtmigo.repr.*
                    fun main() = println(listOf(1, 2, 3).toRepr())                
                """}
    ) as app:
        print(app.files_content())
        result = app.run(["gradle", "run", "-q"])
        print(result)

        assert result.returncode == 0
        assert result.stdout == "listOf(1, 2, 3)\n", result.stdout

    print("Everything is OK!")


def build_test_release():
    outp = json.loads(subprocess.check_output(
        ["java", "-jar", os.path.expanduser("~/mavence.jar"), "local"]))
    test_package(outp["mavenRepo"], outp["version"])


if __name__ == "__main__":
    build_test_release()
