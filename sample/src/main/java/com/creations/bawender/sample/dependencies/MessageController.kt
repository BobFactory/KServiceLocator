package com.creations.bawender.sample.dependencies

class MessageController {
    private val _messages = listOf(
        "This is a injected by a service locator",
        "This can help you abstract your code into services and inject them",
        "this is similar to dependency injections",
        "this is a simpler version of Koin & Dagger",
        "this supports android view-model injections",
        "You can also inject context with this package, just make sure its the app context"
    )

    private var _counter: Int = 0

    fun getLatestCounter(): Int = ++_counter
    fun getRandomMessage(): String = _messages.random()

}