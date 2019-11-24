package com.rnett.delegates

inline fun <T, S> ReadProvider<T>.after(crossinline transform: (T) -> S) = object : ReadProvider<S> {
    override fun getValue(): S {
        return transform(this@after.getValue())
    }
}

inline fun <T, S> ReadWriteProvider<T>.after(crossinline getTransform: (T) -> S, crossinline setTransform: (S) -> T) =
        object : ReadWriteProvider<S> {
            override fun setValue(value: S) {
            this@after.setValue(setTransform(value))
    }

        override fun getValue(): S {
            return getTransform(this@after.getValue())
    }
}
