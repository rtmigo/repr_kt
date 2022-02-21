/**
 * SPDX-FileCopyrightText: (c) 2022 Artёm IG <github.com/rtmigo>
 * SPDX-License-Identifier: Apache-2.0
 **/

package io.github.rtmigo.repr

// avoiding JVM dependencies
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.internal.KotlinReflectionInternalError
import kotlin.reflect.jvm.isAccessible

private fun quotedString(text: String): String =
    "\"${text.replace("\"", "\\\"")}\""


private fun quotedChar(c: Char): String =
    if (c == '\\') "'\\\\'" else "'$c'"


private fun KClass<*>.hasConstructorParameter(name: String) =
    (this.primaryConstructor?.parameters?.firstOrNull { it.name == name } != null)


private fun KClass<*>.hasProperty(name: String) =
    this.declaredMemberProperties.firstOrNull { it.name == name } != null

private fun KClass<*>.findDeclaredMemberProperty(name: String) =
    this.declaredMemberProperties.first { it.name == name }


private fun KClass<*>.hasProperties(): Boolean = !this.declaredMemberProperties.isEmpty()

private class AnalyzedObject(val obj: Any) {
    private val namedParams = mutableListOf<KParameter>()

    /**
     * Will be `true` if for each parameter of primary constructor there is a class property
     * with the same name.
     **/
    var areNamedParamsCorrespondToProperties = true

    init {
        for (param in (obj::class.primaryConstructor?.parameters) ?: listOf()) {
            if (param.name == null)
                continue
            namedParams.add(param)
            if (!obj::class.hasProperty(param.name!!))
                areNamedParamsCorrespondToProperties = false
        }
    }

    /**
     * All the properties that we want to convert as `propertyName=value`.
     **/
    val convertibleProperties: Lazy<Collection<KProperty1<*, *>>> =
        lazy {
            (if (areNamedParamsCorrespondToProperties)
                namedParams.map { obj::class.findDeclaredMemberProperty(it.name!!) }
            else
                obj::class.declaredMemberProperties)

                .filter { prop -> prop.visibility == KVisibility.PUBLIC }
        }
}

private fun reflectConstructorProperties(pre: AnalyzedObject): String {
    // если для каждого аргумента конструктора есть одноименное свойство объекта, вероятно,
    // мы имеем дело чем-то вроде датакласса. Мы сможем легко воспроизвести конструктор вместе
    // со значениями.
    //
    // Если же полного соответствия нет, конвертируем все свойства подряд. Какие из них выжны -
    // непонятно.
    //
    // В итоге мы возможно получим код, похожий на корректный конструктор объекта. Но может и нет.
    //
    // Чтобы исключить ситуацию некорректного конструктора, можно было бы сделать конвертор более
    // строгим: чтобы подобно атрибуту @Serializable он считал ошибкой классы с non-property
    // parameters. Но даже такая проверка, похоже, невозможна в рантайме. Я так думаю, поскольку:
    // 1. Когда Java выполняет свой .toString(), она не возвращает атрибуты конструкторов,
    //    а возвращает все свойства.
    // 2. В вопросе https://stackoverflow.com/questions/71195651 нет ответов
    //
    // Поэтому расслабляемся и генерируем лучшее, что можем.

    val args = pre.convertibleProperties.value
        .mapNotNull { prop -> toAssignmentOrNull(pre.obj, prop) }
        .joinToString(", ")

    return "${pre.obj.javaClass.kotlin.simpleName}($args)"
}


/**
 * Возвращает строку вроде `name=value`. Либо null, если свойство не получается прочитать
 **/
private fun <R> toAssignmentOrNull(obj: Any, property: KCallable<R>): String? {
    val x: R
    try {
        x = property.accessCall(obj)
    } catch (e: KotlinReflectionInternalError) {
        // Caught when trying to read properties of ByteArray:
        //      kotlin.reflect.jvm.internal.KotlinReflectionInternalError:
        //      No accessor found for property val kotlin.ByteArray.size: kotlin.Int
        return null
    }
    return "${property.name}=${x.toRepr()}"
}


/**
 * Возвращает значение свойства, игнорируя значение `KCallable.isAccessible`.
 * Без этого хака мы могли получить `IllegalCallableAccessException`
 **/
private fun <R> KCallable<R>.accessCall(receiver: Any): R {
    // не уверен, как переписывание this.isAccessible сказывается на производительности.
    // На всякий случай избегаю смены значения
    val accessibleOriginal = this.isAccessible
    try {
        if (!accessibleOriginal)
            this.isAccessible = true
        return this.call(receiver)
    } finally {
        if (!accessibleOriginal)
            this.isAccessible = accessibleOriginal
        assert(this.isAccessible == accessibleOriginal)
    }
}

private fun <T> arrayContents(iterable: Sequence<T>): String {
    return iterable.joinToString(", ") { it.toRepr() }
}

private val STRING_KTYPE: KType = String::class.createType()


fun getOwnToRepr(obj: Any): KFunction<String>? {
    val result = obj::class.memberFunctions.firstOrNull {
        it.name == "toRepr" && it.returnType == STRING_KTYPE
    } ?: return null

    // We have checked that the return type is String
    @Suppress("UNCHECKED_CAST")
    return result as KFunction<String>
}

/**
 * Aims to produce a string that is valid Kotlin code. Ideally, this string can be copied into the
 * project's source code to create the same objects.
 **/
fun Any?.toRepr(): String {

    if (this == null) {
        return "null"
    }

    val localToRepr = getOwnToRepr(this)
    if (localToRepr != null) {
        return localToRepr.call(this)
    }

    return when (this) {

        is Map<*, *> ->
            "mapOf(${
                this.entries.joinToString(", ") {
                    "${it.key.toRepr()} to ${it.value.toRepr()}"
                }
            })"

        is List<*> -> "listOf(${
            this.joinToString(", ") { it.toRepr() }
        })"

        is String -> quotedString(this)
        is Char -> quotedChar(this)

        is Byte -> "$this.toByte()"
        is Short -> "$this.toShort()"
        is Int -> this.toString()
        is Long -> this.toString() + "L"

        is UByte -> "$this.toUByte()"
        is UShort -> "$this.toUShort()"
        is UInt -> this.toString() + "U"
        is ULong -> this.toString() + "UL"

        is Float -> this.toString() + 'f'
        is Double -> this.toString()

        is Boolean -> this.toString()
        //is BigDecimal -> "$this.toBigDecimal()"

        is ByteArray -> "byteArrayOf(${
            arrayContents(
                this.iterator().asSequence().map { it.toInt() })
        })"
        is ShortArray -> "shortArrayOf(${
            arrayContents(
                this.iterator().asSequence().map { it.toInt() })
        })"
        is IntArray -> "intArrayOf(${arrayContents(this.iterator().asSequence())})"
        is LongArray -> "longArrayOf(${arrayContents(this.iterator().asSequence())})"

        // unsigned arrays are experimental in 2022-02. Not converting (yet)

        is FloatArray -> "floatArrayOf(${arrayContents(this.iterator().asSequence())})"
        is DoubleArray -> "doubleArrayOf(${arrayContents(this.iterator().asSequence())})"

        else -> {
            val pre = AnalyzedObject(this)
            if (pre.convertibleProperties.value.isNotEmpty()) {
                reflectConstructorProperties(pre)
            }
            else {
                // у объекта нет ни одного свойства. Возможно, его можно создать
                // конструктором MyClass(). Но гадать уже не хочется.
                //
                // Python бы уже вернул
                //      <__main__.Animal object at 0x7f3e4c203cc0>
                //
                // Если объект не переопределял свой toString(), мы вернем что-то вроде
                //      <Animal "com.package$func$Animal@72b16078">
                //      <BigDecimal "3.1415">
                //
                // А если переопределял, то в скобках <> может быть что осмысленное

                return "<${this::class.simpleName} ${this}>"
            }
        }
    }
}


/**
 * "utils.ReprTests$emptyConstructor$Item@5471388b" -> "Item"
 * 3.141593 -> ""
 **/
internal fun defaultToClassName(toStringed: String): String {
    // этот метод не используется с 2022-02. Удалить?

    if (!toStringed.contains('@') || !toStringed.contains('$')) {
        return ""
    }

    return toStringed
        .substringBefore('@')
        .split('$')
        .last()
}
