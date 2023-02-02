package com.creations.bawender.sample

import android.content.Context
import androidx.lifecycle.ViewModel
import com.creations.bawender.sample.dependencies.MessageController
import com.creations.bawender.sample.dependencies.ToastController

class MainViewModel(
    private val toastController: ToastController,
    private val messageController: MessageController,
) : ViewModel() {

    fun showRandomMsg() {
        val message = messageController.getRandomMessage()
        val counter = messageController.getLatestCounter()

        toastController.showToast("counter:$counter, $message")
    }

}