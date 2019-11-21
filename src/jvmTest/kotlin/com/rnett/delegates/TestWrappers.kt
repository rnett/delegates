package com.rnett.delegates

import org.junit.Test
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.test.assertEquals

class TestWrappers {

    data class Wrapper(var value: Int)

    @Test
    fun testVarWrapper(){
        val test = Wrapper(4)

        var testDelegate by test::value

        assertEquals(4, testDelegate, "Read")

        testDelegate = 10
        assertEquals(10, testDelegate, "Delegate set")
        assertEquals(10, test.value, "Wrapped value post-set read")

        test.value = -10
        assertEquals(-10, testDelegate, "Set wrapped value delegate read")
    }

    data class Wrapper2(val value: Int)

    @Test
    fun testValWrapper(){
        val test = Wrapper2(5)

        val testDelegate by test::value

        assertEquals(5, testDelegate, "Read")
    }

    @Test
    fun testValVarWrapper(){
        val test = Wrapper(10)

        val testDelegate by test::value

        assertEquals(10, testDelegate, "Read")
    }

}