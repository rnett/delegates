package com.rnett.delegates

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline class FunctionDelegate<T>(val func: () -> T): ReadOnlyProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return func()
    }
}

