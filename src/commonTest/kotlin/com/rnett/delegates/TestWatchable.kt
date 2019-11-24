package com.rnett.delegates

import kotlin.test.Test
import kotlin.test.assertEquals

class TestWatchable {

    @Test
    fun test() {
        val watch = mutableWrapped(10).mutableWatch()

        var updateCount = 0

        watch.handleUpdate { updateCount++ }

        var value by watch

        assertEquals(0, updateCount)
        value = 12
        assertEquals(1, updateCount)
        assertEquals(12, value)
        watch.refresh()
        assertEquals(2, updateCount)
        watch.update { it - 2 }
        assertEquals(3, updateCount)
    }

}