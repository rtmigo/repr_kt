package io.github.rtmigo.repr

import org.apache.commons.text.translate.*
import java.util.*

private object Escapist {
    private val translator by lazy {
        val escapeJavaMap = HashMap<CharSequence, CharSequence>()
        escapeJavaMap["\""] = "\\\""
        escapeJavaMap["\\"] = "\\\\"
        escapeJavaMap["$"] = "\\$"
        AggregateTranslator(
            LookupTranslator(Collections.unmodifiableMap(escapeJavaMap)),
            LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE),
            JavaUnicodeEscaper.outsideOf(32, 0x7f)
        )
    }

    fun escape(s: String): String = translator.translate(s)
}

internal fun String.escapeSpecialChars(): String = Escapist.escape(this)