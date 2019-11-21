package com.rnett.delegates

import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

data class ValWrapper<T>(val property: KProperty<T>) : WatchableBase<T>(), ReadProvider<T> {
    override fun getValue(): T {
        return this.property.getter.call()
    }
}

fun <T> KProperty<T>.delegate() = ValWrapper(this)

data class VarWrapper<T>(val property: KMutableProperty<T>) : MutableWatchableBase<T>(), ReadWriteProvider<T> {

    override fun justSetValue(value: T) {
        this.property.setter.call(value)
    }

    override fun getValue(): T {
        return this.property.getter.call()
    }
}

fun <T> KMutableProperty<T>.mutableDelegate() = VarWrapper(this)