package com.rnett.delegates

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.test.Test
import kotlin.test.assertEquals

data class WrapperDelegate<T>(var value: T): ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}

class TestTransforms{
    @Test
    fun testReadOnlyTransform(){
        val wrapped = WrapperDelegate("5").asReadOnly()

        val test by wrapped.after { it.toInt() }

        assertEquals(5, test, "Read")
    }

    @Test
    fun testReadWriteTransform(){
        val wrapped = WrapperDelegate("5")

        var test by wrapped.after({it.toInt()}, { it.toString() })

        assertEquals(5, test, "Read")

        test = 10

        assertEquals(10, test, "Set")
        assertEquals("10", wrapped.value, "Wrapped Set")

        wrapped.value = "20"

        assertEquals(20, test, "Delegated Read of wrapped set")

    }
}