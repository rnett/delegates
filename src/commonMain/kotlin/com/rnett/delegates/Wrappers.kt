package com.rnett.delegates

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class Wrapper<out T>(open val value: T) : ReadOnlyProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }
}

fun <T> wrapped(value: T) = Wrapper(value)

class MutableWrapper<T>(override var value: T) : Wrapper<T>(value), ReadWriteProperty<Any?, T> {
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}

fun <T> mutableWrapped(value: T) = MutableWrapper(value)