# uses https://github.com/rtmigo/kitest_py

from kitest import run_with_git_dependency

result = run_with_git_dependency(
    module="io.github.rtmigo:repr",
    url="https://github.com/rtmigo/repr_kt",
    main_kt="""
        import io.github.rtmigo.repr.*
        fun main() = println(listOf(1, 2, 3).toRepr())
    """)

if result.text.strip() != "listOf(1, 2, 3)":
    exit(1)

print("Everything is OK!")
