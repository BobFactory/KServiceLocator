package com.creations.bawender.sample

import android.app.Application
import com.creations.bawender.sample.dependencies.MessageController
import com.creations.bawender.sample.dependencies.ToastController
import com.creations.bawender.service_locator.*
import com.creations.bawender.service_locator.helpers.get

class MainApp: Application() {

    override fun onCreate() {
        super.onCreate()

        with(ServiceLocator) {
            //Global Context stored as a singleton
            androidContext(this@MainApp)

            //Dependencies in order of required sub dependencies
            factory { ToastController(get()) }
            single { MessageController() }

            //View model
            viewModel { MainViewModel(get(), get()) }
        }
    }
}