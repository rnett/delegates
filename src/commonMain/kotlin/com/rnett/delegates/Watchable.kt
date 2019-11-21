package com.rnett.delegates

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

//TODO make everything not watchable by default (lots of state, this is bad)
//  just easily wrap it in one
//  still need a better way of dealing with name conflicts

interface Watchable<T> : ReadProvider<T> {
    fun handleUpdate(handler: (T) -> Unit)
    fun refresh(newValue: T? = null)
}

interface MutableWatchable<T> : Watchable<T>, ReadWriteProvider<T>

inline fun <T> Watchable<T>.update(update: (T) -> Unit) {
    val value = getValue()
    update(value)
    refresh(getValue())
}

abstract class WatchableBase<T>() : Watchable<T> {

    private val handlers = mutableListOf<(T) -> Unit>()

    override fun handleUpdate(handler: (T) -> Unit) {
        handlers += handler
    }

    override fun refresh(newValue: T?) {
        val value = newValue ?: getValue()
        handlers.forEach { it(value) }
    }
}

abstract class MutableWatchableBase<T> : WatchableBase<T>(), MutableWatchable<T> {
    final override fun setValue(value: T) {
        justSetValue(value)
        refresh(value)
    }

    protected abstract fun justSetValue(value: T)
}


data class DelegateHelper<O, R, T : Any>(val base: O, val make: (R, KProperty<*>, O) -> T) {
    lateinit var value: T

    operator fun getValue(thisRef: R, property: KProperty<*>): T {
        if (!this::value.isInitialized)
            value = make(thisRef, property, base)

        return value
    }
}

data class WatchableReadWritePropertyWrapper<R, T>(
    val thisRef: R,
    val property: KProperty<*>,
    val base: ReadWriteProperty<R, T>
) :
    MutableWatchableBase<T>() {

    override fun getValue(): T {
        return base.getValue(thisRef, property)
    }

    override fun justSetValue(value: T) {
        base.setValue(thisRef, property, value)
        refresh(value)
    }
}

fun <R, T> ReadWriteProperty<R, T>.asWatchable() =
    DelegateHelper(this) { t: R, p, o -> WatchableReadWritePropertyWrapper(t, p, o) }

data class WatchableReadOnlyPropertyWrapper<R, T>(
    val thisRef: R,
    val property: KProperty<*>,
    val base: ReadOnlyProperty<R, T>
) :
    WatchableBase<T>(), ReadProvider<T> {

    override fun getValue(): T {
        return base.getValue(thisRef, property)
    }
}

//TODO docs
fun <R, T> ReadOnlyProperty<R, T>.asWatchable() =
    DelegateHelper(this) { t: R, p, o -> WatchableReadOnlyPropertyWrapper(t, p, o) }