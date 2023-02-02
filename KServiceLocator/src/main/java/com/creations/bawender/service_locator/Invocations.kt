package com.creations.bawender.service_locator

interface Invocation<T> {
    fun get(): T
}

class FactoryInvocation<T>(private val initFunc: () -> T): Invocation<T> {

    override fun get(): T = initFunc()

    override fun toString(): String {
        return "FactoryInvocation( ${get()} )"
    }
}

class SingleInvocation<T>(initFunc: () -> T): Invocation<T> {

    private val dependency: T = initFunc()

    override fun get(): T = dependency

    override fun toString(): String {
        return "SingleInvocation( ${get()} )"
    }
}
