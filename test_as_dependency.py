# uses https://github.com/rtmigo/kitest_py

import sys
from kitest import *

with AppWithGitDependency(
    module="io.github.rtmigo:repr",
    url="https://github.com/rtmigo/repr_kt",
    branch=sys.argv[1],
    main_kt="""
        import io.github.rtmigo.repr.*
        fun main() = println(listOf(1, 2, 3).toRepr())
    """) as app:

    app.run().assert_output_is("listOf(1, 2, 3)\n")

print("Everything is OK!")
