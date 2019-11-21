package com.rnett.delegates.legacy

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

data class ToReadOnlyDelegate<in R, T>(val base: ReadWriteProperty<R, T>) : ReadOnlyProperty<R, T> {
    override fun getValue(thisRef: R, property: KProperty<*>): T {
        return base.getValue(thisRef, property)
    }
}

data class LegacyReadOnlyTransform<in R, T, out S>(val base: ReadOnlyProperty<R, T>, val transform: (T) -> S) :
    ReadOnlyProperty<R, S> {
    override fun getValue(thisRef: R, property: KProperty<*>): S {
        return transform(base.getValue(thisRef, property))
    }
}

@Deprecated(message = "Use ReadProvider if possible")
fun <R, T, S> ReadOnlyProperty<R, T>.after(transform: (T) -> S) = LegacyReadOnlyTransform(this, transform)

data class LegacyReadWriteTransform<in R, T, S>(
    val base: ReadWriteProperty<R, T>,
    val getTransform: (T) -> S,
    val setTransform: (S) -> T
) :
    ReadWriteProperty<R, S> {
    override fun getValue(thisRef: R, property: KProperty<*>): S {
        return getTransform(base.getValue(thisRef, property))
    }

    override fun setValue(thisRef: R, property: KProperty<*>, value: S) {
        base.setValue(thisRef, property, setTransform(value))
    }
}

@Deprecated(message = "Use ReadWriteProvider if possible")
fun <R, T, S> ReadWriteProperty<R, T>.after(getTransform: (T) -> S, setTransform: (S) -> T) =
    LegacyReadWriteTransform(this, getTransform, setTransform)

fun <R, T> ReadWriteProperty<R, T>.asReadOnly() = ToReadOnlyDelegate(this)

