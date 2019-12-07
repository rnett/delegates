package com.rnett.delegates

import kotlin.random.Random


interface Watchable<T> : ReadProvider<T> {
    fun handleUpdate(handler: (T) -> Unit): Int
    fun refresh(newValue: T? = null)
    fun removeHandler(handler: (T) -> Unit)
    fun removeHandler(handlerId: Int)
}

interface MutableWatchable<T> : Watchable<T>, ReadWriteProvider<T>

inline fun <T> Watchable<T>.update(update: (T) -> Unit) {
    val value = getValue()
    update(value)
    refresh(getValue())
}

open class WatchableBase<T>(open val provider: ReadProvider<T>) : Watchable<T> {

    private val handlers = mutableMapOf<Int, (T) -> Unit>()

    override fun handleUpdate(handler: (T) -> Unit): Int {
        var id = Random.nextInt()

        while (id in handlers) {
            id = Random.nextInt()
        }

        handlers[id] = handler
        return id
    }

    override fun refresh(newValue: T?) {
        val value = newValue ?: getValue()
        handlers.values.forEach { it(value) }
    }

    override fun getValue(): T {
        return provider.getValue()
    }

    override fun removeHandler(handler: (T) -> Unit) {
        handlers.entries.filter { it.value === handler || it.value == handler }.map { it.key }.forEach {
            handlers.remove(it)
        }
    }

    override fun removeHandler(handlerId: Int) {
        handlers.remove(handlerId)
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