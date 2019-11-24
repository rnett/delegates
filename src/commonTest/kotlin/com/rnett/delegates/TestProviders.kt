package com.rnett.delegates

import kotlin.properties.Delegates
import kotlin.test.Test
import kotlin.test.assertEquals

class TestProviders {
    @Test
    fun testReadOnly() {
        val provider by Delegates.observable(10) { _, _, _ -> }.asProvider()

        val middle = provider.after { it.toString() }

        val value by middle

        assertEquals("10", value)
    }

    @Test
    fun testReadWrite() {
        val provider by Delegates.observable(10) { _, _, _ -> }.asProvider()

        val middle = provider.after({ it.toString() }, { it.toInt() })

        var value by middle

        assertEquals("10", value)
        value = "12"
        assertEquals("12", value)
    }
}