package com.rnett.delegates

import kotlin.test.Test
import kotlin.test.assertEquals

class TestMapDelegates {

    @Test
    fun testGetOrPutDelegate(){
        val map = mutableMapOf<Int, String>(3 to "a", 6 to "abcd", 12 to "hello")

        var a by map.getOrPutDelegate(3){"hi"}

        assertEquals("a", a)
        a = "twinkle"
        assertEquals("twinkle", map.getValue(3))

        val b by map.getOrPutDelegate(22){ "b" }

        assertEquals("b", b)
        assertEquals("b", map.getValue(22))

//        val stringMap = mutableMapOf<String, Int>("test" to 5, "hello" to 12)
//
//        val c by stringMap.getOrPutDelegate { 20 }
//
//        assertEquals(c, 20)
//        assertEquals(stringMap["c"], 20)
//
//        val test by stringMap.getOrPutDelegate { 22 }
//
//        assertEquals(test, 5)
//
//        val t2: Int? by stringMap
    }

    @Test
    fun testOptionalDelegates(){
        //TODO non string map
//        val map = mutableMapOf<String, Int>("a" to 3)
//
//        var a by map.optionalStringRWDelegate()
//        var b by map.optionalStringRWDelegate()
//
//        assertEquals(3, a)
//        assertEquals(null, b)
//
//        a = 5
//        b = 11
//
//        assertEquals(map["a"], 5)
//        assertEquals(map["b"], 11)

    }

}