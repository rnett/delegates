package com.rnett.delegates

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


interface ReadProvider<out T> {
    fun getValue(): T
}

inline operator fun <S : ReadProvider<T>, T> S.getValue(thisRef: Any?, property: KProperty<*>): T {
    return getValue()
}

interface ReadWriteProvider<T> : ReadProvider<T> {
    fun setValue(value: T)

}

inline operator fun <S : ReadWriteProvider<T>, T> S.setValue(thisRef: Any?, property: KProperty<*>, value: T) {
    setValue(value)
}

data class DelegateHelper<O, R, T : Any>(val base: O, val make: (R, KProperty<*>, O) -> T) {
    lateinit var value: T

    operator fun getValue(thisRef: R, property: KProperty<*>): T {
        if (!this::value.isInitialized)
            value = make(thisRef, property, base)

        return value
    }
}

class ReadOnlyDelegateWrapper<T, R>(val delegate: ReadOnlyProperty<R, T>, val thisRef: R, val property: KProperty<*>) : ReadProvider<T> {
    override fun getValue(): T {
        return delegate.getValue(thisRef, property)
    }
}

class ReadWriteDelegateWrapper<T, R>(val delegate: ReadWriteProperty<R, T>, val thisRef: R, val property: KProperty<*>)
    : ReadWriteProvider<T> {
    override fun getValue(): T {
        return delegate.getValue(thisRef, property)
    }

    override fun setValue(value: T) {
        delegate.setValue(thisRef, property, value)
    }
}

/**
 * Converts a ReadOnlyProperty to a delegate of a ReadProvider.
 * The extra delegation is nessecary to extract the thisRef and property parameters, and as such
 * the thisRef and property values passed to the delegate will be the values of the first delegation
 * (the one that gets the provider) not the second (the one that gets the value)
 *
 * Example:
 * ```
 *  val provider by Delegates.observable(3){_, _, _ ->}.asProvider() // thisRef and property are set here
 *  val value: Int by provider
 * ```
 *
 */
inline fun <R, T> ReadOnlyProperty<R, T>.asProvider() = DelegateHelper(this) { t: R, p, d -> ReadOnlyDelegateWrapper(d, t, p) }

/**
 * Converts a ReadWriteProperty to a delegate of a ReadWriteProvider.
 * The extra delegation is nessecary to extract the thisRef and property parameters, and as such
 * the thisRef and property values passed to the delegate will be the values of the first delegation
 * (the one that gets the provider) not the second (the one that gets the value)
 *
 * Example:
 * ```
 *  val provider by Delegates.observable(3){_, _, _ ->}.asProvider() // thisRef and property are set here
 *  val value: Int by provider
 * ```
 *
 */
inline fun <R, T> ReadWriteProperty<R, T>.asProvider() = DelegateHelper(this) { t: R, p, d -> ReadWriteDelegateWrapper(d, t, p) }