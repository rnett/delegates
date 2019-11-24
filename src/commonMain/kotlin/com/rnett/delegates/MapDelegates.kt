package com.rnett.delegates

data class GetOrPutDelegate<K, T>(val key: K, val map: MutableMap<K, T>, val initialValue: () -> T) :
        ReadWriteProvider<T> {
    override fun getValue(): T {
        return map.getOrPut(key, initialValue)
    }

    override fun setValue(value: T) {
        map[key] = value
    }
}

fun <K, V> MutableMap<K, V>.getOrPutDelegate(key: K, initialValue: () -> V) = GetOrPutDelegate(key, this, initialValue)
//
//data class StringGetOrPutDelegate<T>(val map: MutableMap<String, T>, val initialValue: () -> T): ReadWriteProperty<Any?, T> {
//    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
//        return map.getOrPut(property.name, initialValue)
//    }
//
//    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
//        map[property.name] = value
//    }
//}
//
//fun <V> MutableMap<String, V>.getOrPutDelegate(initialValue: () -> V) = StringGetOrPutDelegate(this, initialValue)

data class OptionalDelegate<K, V>(val map: MutableMap<K, V>, val key: K) : ReadWriteProvider<V?> {
    override fun getValue(): V? {
        return map[key]
    }

    override fun setValue(value: V?) {
        try{
            map[key] = value as V
        } catch (e: ClassCastException){
            if(value != null)
                map[key] = value
        }
    }
}

fun <K, V> MutableMap<K, V>.optionalRWDelegate(key: K) = OptionalDelegate(this, key)
fun <K, V> Map<K, V>.optionalDelegate(key: K) = functionDelegate { get(key) }
//
//class StringOptionalDelegate<V>(override val map: MutableMap<String, V>) : StringReadOnlyOptionalDelegate<V>(map),
//    ReadWriteProperty<Any?, V?> {
//    override fun setValue(thisRef: Any?, property: KProperty<*>, value: V?) {
//        try{
//            map[property.name] = value as V
//        } catch (e: ClassCastException){
//            if(value != null)
//                map[property.name] = value
//        }
//    }
//}
//
//open class StringReadOnlyOptionalDelegate<V>(open val map: Map<String, V>) : ReadOnlyProperty<Any?, V?> {
//    override fun getValue(thisRef: Any?, property: KProperty<*>): V? {
//        return map[property.name]
//    }
//}
//
//fun <V> MutableMap<String, V>.optionalStringRWDelegate() = StringOptionalDelegate(this)
//fun <V> Map<String, V>.optionalStringDelegate() = StringReadOnlyOptionalDelegate(this)
//
//fun <V> MutableMap<String, V>.optionalStringRWDelegate(key: String?): ReadWriteProperty<Any?, V?> =
//    if (key == null) optionalStringRWDelegate() else optionalStringRWDelegate(key)
//
//fun <V> Map<String, V>.optionalStringDelegate(key: String?): ReadOnlyProperty<Any?, V?> =
//    if (key == null) optionalStringDelegate() else optionalDelegate(key)

open class RORequiredDelegate<K, V>(open val map: Map<K, V>, val key: K) : ReadProvider<V> {
    override fun getValue(): V {
        return map.getValue(key)
    }

}

class RWRequiredDelegate<K, V>(override val map: MutableMap<K, V>, key: K) : RORequiredDelegate<K, V>(map, key),
        ReadWriteProvider<V> {
    override fun setValue(value: V) {
        map[key] = value
    }
}

fun <K, V> MutableMap<K, V>.rwDelegate(key: K) = RWRequiredDelegate(this, key)
fun <K, V> Map<K, V>.delegate(key: K) = RORequiredDelegate(this, key)

//open class ROStringRequiredDelegate<out V>(open val map: Map<String, V>) : ReadOnlyProperty<Any?, V> {
//    override fun getValue(thisRef: Any?, property: KProperty<*>): V {
//        return map.getValue(property.name)
//    }
//
//}
//
//class RWStringRequiredDelegate<V>(override val map: MutableMap<String, V>) : ROStringRequiredDelegate<V>(map),
//    ReadWriteProperty<Any?, V> {
//    override fun setValue(thisRef: Any?, property: KProperty<*>, value: V) {
//        map[property.name] = value
//    }
//}
//
//fun <V> MutableMap<String, V>.stringRWDelegate() = RWStringRequiredDelegate(this)
//fun <V> Map<String, V>.stringDelegate() = ROStringRequiredDelegate(this)
//
//fun <V> MutableMap<String, V>.stringRWDelegate(key: String?): ReadWriteProperty<Any?, V> =
//    if (key == null) stringRWDelegate() else rwDelegate(key)
//
//fun <V> Map<String, V>.stringDelegate(key: String?): ReadOnlyProperty<Any?, V> =
//    if (key == null) stringDelegate() else delegate(key)
