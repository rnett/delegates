package com.rnett.delegates

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


interface ReadProvider<out T> : ReadOnlyProperty<Any?, T> {
    fun getValue(): T

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getValue()
    }
}

interface ReadWriteProvider<T> : ReadProvider<T>, ReadWriteProperty<Any?, T> {
    fun setValue(value: T)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        setValue(value)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getValue()
    }
}

inline class FunctionDataProvider<T>(val function: () -> T) : ReadProvider<T> {
    override fun getValue() = function()
}

