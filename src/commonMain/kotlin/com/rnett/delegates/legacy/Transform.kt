package com.rnett.delegates.legacy

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun <R, T, S> ReadOnlyProperty<R, T>.after(crossinline transform: (T) -> S) = object : ReadOnlyProperty<R, S> {
    override inline fun getValue(thisRef: R, property: KProperty<*>): S = transform(this@after.getValue(thisRef, property))
}

inline fun <R, T, S> ReadWriteProperty<R, T>.after(crossinline getTransform: (T) -> S, crossinline setTransform: (S) -> T) = object : ReadWriteProperty<R, S> {
    override inline fun getValue(thisRef: R, property: KProperty<*>): S = getTransform(this@after.getValue(thisRef, property))

    override inline fun setValue(thisRef: R, property: KProperty<*>, value: S) {
        this@after.setValue(thisRef, property, setTransform(value))
    }
}

fun <R, T> ReadWriteProperty<R, T>.asReadOnly() = object : ReadOnlyProperty<R, T> {
    override inline fun getValue(thisRef: R, property: KProperty<*>): T = this@asReadOnly.getValue(thisRef, property)
}

