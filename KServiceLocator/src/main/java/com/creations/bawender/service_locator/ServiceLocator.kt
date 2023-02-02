package com.creations.bawender.service_locator

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlin.reflect.typeOf

object ServiceLocator {
    private val dependencies = mutableMapOf<String, Invocation<*>>()

    val hasMappings: Boolean
        get() = dependencies.isNotEmpty()

    inline fun <reified T> getKey(): String = typeOf<T>().classifier.toString()

    fun <T> putMapping(key: String, invocation: Invocation<T>) {
        synchronized(this) {
            dependencies[key] = invocation
        }
    }

    fun hasMappingFor(key: String): Boolean =
        dependencies.containsKey(key)

    fun getMapping(key: String): Invocation<*> {
        synchronized(this) {
            return dependencies[key]!!
        }
    }

    fun clearMappings() {
        synchronized(this) {
            dependencies.clear()
        }
    }

}

inline fun <reified T> ServiceLocator.factory(noinline block: () -> T) {
    putMapping(getKey<T>(), FactoryInvocation(block))
}

inline fun <reified T> ServiceLocator.single(noinline block: () -> T) {
    putMapping(getKey<T>(), SingleInvocation(block))
}

inline fun <reified VM : ViewModel> ServiceLocator.viewModel(noinline block: (SavedStateHandle) -> VM) {
    val factory = FactoryInvocation {
        viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                block(savedStateHandle)
            }
        }
    }

    putMapping(getKey<VM>(), factory)
}

fun ServiceLocator.androidContext(context: Context) {
    single { context }
}




