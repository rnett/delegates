package com.rnett.delegates

inline fun <T> functionDelegate(crossinline func: () -> T) = object : WatchableBase<T>() {
    override fun getValue(): T {
        return func()
    }

}

