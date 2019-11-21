package com.rnett.delegates

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

data class GetOrPutDelegate<K, T>(val key: K, val map: MutableMap<K, T>, val initialValue: () -> T): ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return map.getOrPut(key, initialValue)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        map[key] = value
    }
}

fun <K, V> MutableMap<K, V>.getOrPutDelegate(key: K, initialValue: () -> V) = GetOrPutDelegate(key, this, initialValue)

data class StringGetOrPutDelegate<T>(val map: MutableMap<String, T>, val initialValue: () -> T): ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return map.getOrPut(property.name, initialValue)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        map[property.name] = value
    }
}

fun <V> MutableMap<String, V>.getOrPutDelegate(initialValue: () -> V) = StringGetOrPutDelegate(this, initialValue)

data class OptionalDelegate<K, V>(val map: MutableMap<K, V>, val key: K): ReadWriteProperty<Any?, V?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): V? {
        return map[key]
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: V?) {
        try{
            map[key] = value as V
        } catch (e: ClassCastException){
            if(value != null)
                map[key] = value
        }
    }
}

fun <K, V> MutableMap<K, V>.optionalDelegate(key: K) = OptionalDelegate(this, key)
fun <K, V> Map<K, V>.optionalDelegate(key: K) = FunctionDelegate { get(key) }

data class StringOptionalDelegate<V>(val map: MutableMap<String, V>): ReadWriteProperty<Any?, V?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): V? {
        return map[property.name]
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: V?) {
        try{
            map[property.name] = value as V
        } catch (e: ClassCastException){
            if(value != null)
                map[property.name] = value
        }
    }
}

data class StringReadOnlyOptionalDelegate<V>(val map: Map<String, V>): ReadOnlyProperty<Any?, V?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): V? {
        return map[property.name]
    }
}

fun <V> MutableMap<String, V>.optionalDelegate() = StringOptionalDelegate(this)
fun <V> Map<String, V>.optionalDelegate() = StringReadOnlyOptionalDelegate(this)
