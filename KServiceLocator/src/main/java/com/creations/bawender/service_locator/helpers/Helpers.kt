package com.creations.bawender.service_locator.helpers

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.creations.bawender.service_locator.ServiceLocator


inline fun <reified T> get(): T {
    val key = ServiceLocator.getKey<T>()

    return when {
        ServiceLocator.hasMappingFor(key) -> {
            ServiceLocator.getMapping(key).get() as T
        }
        else -> {
            error("Missing dependency for class $key")
        }
    }
}

inline fun <reified T> inject() = lazy { get<T>() }

inline fun <reified T : ViewModel> ComponentActivity.getViewModel(): T {

    val key = ServiceLocator.getKey<T>()

    return when {
        ServiceLocator.hasMappingFor(key) -> {
            val factory = getFactory<T>()
            ViewModelProvider(this, factory = factory)[T::class.java]
        }

        else -> {
            error("Missing dependency for class $key")
        }
    }
}


