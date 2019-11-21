package com.rnett.delegates

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

data class ValWrapper<T>(val property: KProperty<T>): ReadOnlyProperty<Any?, T>{
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return this.property.getter.call()
    }
}

operator fun <T> KProperty<T>.provideDelegate(thisRef: Any?, prop: KProperty<*>)  = ValWrapper(this)

data class VarWrapper<T>(val property: KMutableProperty<T>): ReadWriteProperty<Any?, T>{
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return this.property.getter.call()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.property.setter.call(value)
    }
}

operator fun <T> KMutableProperty<T>.provideDelegate(thisRef: Any?, prop: KProperty<*>)  = VarWrapper(this)