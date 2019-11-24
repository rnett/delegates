package com.rnett.delegates

inline fun <T> functionDelegate(crossinline getter: () -> T) = object : ReadProvider<T> {
    override fun getValue(): T {
        return getter()
    }
}

inline fun <T> readWriteDelegate(crossinline getter: () -> T, crossinline setter: (T) -> Unit) = object : ReadWriteProvider<T> {
    override fun getValue(): T {
        return getter()
    }

    override fun setValue(value: T) {
        setter(value)
    }
}

