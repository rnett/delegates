package com.rnett.delegates

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

data class ReadOnlyTransform<in R, T, out S>(val base: ReadOnlyProperty<R, T>, val transform: (T) -> S): ReadOnlyProperty<R, S>{
    override fun getValue(thisRef: R, property: KProperty<*>): S {
        return transform(base.getValue(thisRef, property))
    }
}

fun <R, T, S> ReadOnlyProperty<R, T>.after(transform: (T) -> S) = ReadOnlyTransform(this, transform)

data class ReadWriteTransform<in R, T, S>(val base: ReadWriteProperty<R, T>, val getTransform: (T) -> S, val setTransform: (S) -> T): ReadWriteProperty<R, S> {
    override fun getValue(thisRef: R, property: KProperty<*>): S {
        return getTransform(base.getValue(thisRef, property))
    }

    override fun setValue(thisRef: R, property: KProperty<*>, value: S) {
        base.setValue(thisRef, property, setTransform(value))
    }
}

fun <R, T, S> ReadWriteProperty<R, T>.after(getTransform: (T) -> S, setTransform: (S) -> T) = ReadWriteTransform(this, getTransform, setTransform)

data class ToReadOnlyDelegate<in R, T>(val base: ReadWriteProperty<R, T>): ReadOnlyProperty<R, T> {
    override fun getValue(thisRef: R, property: KProperty<*>): T {
        return base.getValue(thisRef, property)
    }
}

fun <R, T> ReadWriteProperty<R, T>.asReadOnly() = ToReadOnlyDelegate(this)

fun <R, T, S> ReadWriteProperty<R, T>.after(transform: (T) -> S) = ReadOnlyTransform(this.asReadOnly(), transform)

