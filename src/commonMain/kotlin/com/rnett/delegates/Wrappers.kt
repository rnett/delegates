package com.rnett.delegates

open class Wrapper<T>(open val wrappedValue: T) : ReadProvider<T> {
    override fun getValue(): T {
        return wrappedValue
    }
}

fun <T> wrapped(value: T) = Wrapper(value)

class MutableWrapper<T>(override var wrappedValue: T) : Wrapper<T>(wrappedValue), ReadWriteProvider<T> {
    override fun setValue(value: T) {
        this.wrappedValue = value
    }
}

fun <T> mutableWrapped(value: T) = MutableWrapper(value)