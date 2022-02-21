/**
 * SPDX-FileCopyrightText: (c) 2022 Artёm IG <github.com/rtmigo>
 * SPDX-License-Identifier: Apache-2.0
 **/


import org.junit.jupiter.api.Test
import io.github.rtmigo.repr.*
import kotlin.test.*

@JvmInline
private value class IntValueClass(val x: Int)

class ReprTests {
    @Test
    fun `tree with list and map`() {
        //throw Exception()
        data class Item(val city: String, val year: Int)

        val src = mapOf(
            "Minsk" to listOf(
                Item(city = "Minsk", year = 1990),
                Item(city = "Minsk", year = 1999),
                null
            ),
            "Warsaw" to listOf(
                Item(city = "Warsaw", year = 1995),
                null,
                null
            ),
            "Kyiv" to listOf(
                Item(city = "Kyiv", year = 1998),
                null,
                Item(city = "Kyiv", year = 2010)
            ),
            "Grodno" to listOf(
                null,
                Item(city = "Grodno", year = 1994),
                null
            ),
            "Moscow" to listOf(
                null,
                Item(city = "Moscow", year = 2001),
                null
            ),
            "Lviv" to listOf(
                null,
                Item(city = "Lviv", year = 2003),
                Item(city = "Lviv", year = 2012)
            )
        )

        assertEquals(
            """mapOf("Minsk" to listOf(Item(city="Minsk", year=1990), Item(city="Minsk", year=1999), null), "Warsaw" to listOf(Item(city="Warsaw", year=1995), null, null), "Kyiv" to listOf(Item(city="Kyiv", year=1998), null, Item(city="Kyiv", year=2010)), "Grodno" to listOf(null, Item(city="Grodno", year=1994), null), "Moscow" to listOf(null, Item(city="Moscow", year=2001), null), "Lviv" to listOf(null, Item(city="Lviv", year=2003), Item(city="Lviv", year=2012)))""",
            src.toRepr()
        )
    }

    @Test
    fun `extra public property ignored if all constructor parameters correspond to properties`() {
        class Item(val city: String, val year: Int) {
            val extra = "$city!" // это не должно быть в строке
        }

        val src = Item(city = "Minsk", year = 2022)

        assertEquals(
            """Item(city="Minsk", year=2022)""",
            src.toRepr()
        )
    }

    @Test
    fun `class with constructor parameter that has default value`() {
        class Item(val city: String, val year: Int = 1981) {
            val extra = "$city!" // это не должно быть в строке
        }

        val src = Item(city = "Minsk")

        assertEquals(
            """Item(city="Minsk", year=1981)""",
            src.toRepr()
        )
    }

    @Test
    fun `when constructor has a non-property parameter, all properties are converted`() {
        // тут с конструктором не сложилось, поэтому класс вернет все свойства
        class Item(cityParam: String, val year: Int = 1981) {
            val cityProp = cityParam.uppercase()
        }

        val src = Item(cityParam = "Minsk")

        assertEquals(
            """Item(cityProp="MINSK", year=1981)""",
            src.toRepr()
        )
    }

    @Test
    fun `constructor has non-property parameter with the same name as a property`() {
        class Item(city: String, // not a property
                   val year: Int = 1981) {
            val city = city.uppercase() // a property with the same name
        }

        // actually this is a problem, but that's the best we can do
        val src = Item(city = "Minsk")
        assertEquals(
            """Item(city="MINSK", year=1981)""",
            src.toRepr()
        )
    }

    @Test
    fun `empty constructor`() {
        class Item

        val src = Item()
        assertTrue(src.toRepr().contains("\$Item@"))
    }

    @Test
    fun `constructor with vararg`() {
        class Item(vararg val args: Int)

        val src = Item(1, 2, 3)
        assertEquals("Item(args=intArrayOf(1, 2, 3))", src.toRepr())
    }

    @Test
    fun arrays() {
        assertEquals("byteArrayOf(23, 42)", byteArrayOf(23, 42).toRepr())
        assertEquals("shortArrayOf(23, 42)", shortArrayOf(23, 42).toRepr())
        assertEquals("intArrayOf(23, 42)", intArrayOf(23, 42).toRepr())
        assertEquals("longArrayOf(23L, 42L)", longArrayOf(23, 42).toRepr())

        assertEquals("floatArrayOf(23.0f, 42.0f)", floatArrayOf(23.0f, 42.0f).toRepr())
        assertEquals("doubleArrayOf(23.0, 42.0)", doubleArrayOf(23.0, 42.0).toRepr())
    }


    @Test
    fun `value class`() {
        val src = IntValueClass(10)
        src.x
        assertEquals(
            "IntValueClass(x=10)",
            src.toRepr()
        )
    }

    @Test
    fun decimal() {
        val src = 3.141593.toBigDecimal()

        assertEquals(
            "3.141593.toBigDecimal()",
            src.toRepr()
        )
    }

    @Test
    fun double() {
        assertEquals(
            "3.141593",
            3.141593.toRepr()
        )
    }

    @Test
    fun bytearray() {
        assertEquals(
            "byteArrayOf(1, 2, 3)", // ну пока так
            byteArrayOf(1, 2, 3).toRepr()
        )
    }


    @Test
    fun listOfNumbers() {
        val src = listOf(123.45, 500, -2L, 13UL, null, 'x', 37.7f)

        assertEquals(
            "listOf(123.45, 500, -2L, 13UL, null, 'x', 37.7f)",
            src.toRepr()
        )
    }

    @Test
    fun boolean() {
        assertEquals("true", true.toRepr())

        assertEquals("false", false.toRepr())
    }

    @Test
    fun nullVal() {
        assertEquals("null", null.toRepr())
    }

    @Test
    fun byte() {
        assertEquals("42.toByte()", 42.toByte().toRepr())
    }

    @Test
    fun uint() {
        assertEquals("42U", 42U.toRepr())
    }

    @Test
    fun short() {
        assertEquals("4242.toShort()", 4242.toShort().toRepr())
    }

    @Test
    fun ubyte() {
        assertEquals("42.toUByte()", 42.toUByte().toRepr())
    }

    @Test
    fun ushort() {
        assertEquals(            "4242.toUShort()", 4242.toUShort().toRepr()        )
    }


    @Test
    fun defaultToClassName() {

        class ItemA

        assertEquals(
            "ItemA",
            defaultToClassName(ItemA().toString())
        )

        class ItemB(val x: Int)

        assertEquals(
            "ItemB",
            defaultToClassName(ItemB(5).toString())
        )

        assertEquals(
            "",
            defaultToClassName(3.141593.toString())
        )
    }

    @Test
    fun `class with own toRepr() implementation`() {

        class MyClass(ab: String) {
            val a = ab[0].digitToInt()
            val b = ab[2].digitToInt()

            fun toRepr(): String {
                return "MyClass(\"${"$a|$b"}\")"
            }
        }

        assertEquals(
            """MyClass("5|7")""",
            MyClass("5|7").toRepr()
        )

        assertEquals(
            """listOf(MyClass("5|7"), MyClass("1|2"))""",
            listOf(MyClass("5|7"), MyClass("1|2")).toRepr()
        )
    }

    @Test
    fun `class with own toRepr() implementation with non-string return type`() {

        class MyClass(val x: Int) {
            fun toRepr(): Int = x*2
        }

        // direct call will return integer (because it has nothing with
        // the extension method .toRepr)
        assertEquals(6, MyClass(3).toRepr())

        // extension method will not use toRepr: Int
        assertEquals("""MyClass(x=2)""",
                     (MyClass(2) as Any).toRepr())
        assertEquals("""listOf(MyClass(x=2), MyClass(x=3))""",
                     listOf(MyClass(2), MyClass(3)).toRepr())
    }

    @Test
    fun `class with private properties`() {

        class MyClass(val x: Int) {
            private val y = x*2
            private val z = x/2
        }

        assertEquals("""MyClass(x=2)""",
                     MyClass(2).toRepr())

    }

    @Test
    fun `class with protected properties`() {

        class MyClass(val x: Int) {
            protected val y = x*2
            protected val z = x/2
        }

        assertEquals("""MyClass(x=2)""",
                     MyClass(2).toRepr())

    }
}

