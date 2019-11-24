package com.rnett.delegates


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

open class WatchableBase<T>(open val provider: ReadProvider<T>) : Watchable<T> {

    private val handlers = mutableListOf<(T) -> Unit>()

    override fun handleUpdate(handler: (T) -> Unit) {
        handlers += handler
    }

    override fun refresh(newValue: T?) {
        val value = newValue ?: getValue()
        handlers.forEach { it(value) }
    }

    override fun getValue(): T {
        return provider.getValue()
    }
}

class MutableWatchableBase<T>(override val provider: ReadWriteProvider<T>) : WatchableBase<T>(provider), MutableWatchable<T> {
    override fun setValue(value: T) {
        provider.setValue(value)
        refresh(value)
    }
}

fun <T> ReadProvider<T>.watch() = WatchableBase(this)
fun <T> ReadWriteProvider<T>.mutableWatch() = MutableWatchableBase(this)