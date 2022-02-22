# uses https://github.com/rtmigo/kitest_py

from kitest import run_with_git_dependency

with AppWithGitDependency(
    module="io.github.rtmigo:repr",
    url="https://github.com/rtmigo/repr_kt",
    branch="dev",
    main_kt="""
        import io.github.rtmigo.repr.*
        fun main() = println(listOf(1, 2, 3).toRepr())
    """) as app:

    app.run().assert_output_is("listOf(1, 2, 3)")

print("Everything is OK!")
