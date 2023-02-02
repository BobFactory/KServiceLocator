package com.creations.bawender.sample.dependencies

import android.content.Context
import android.widget.Toast

class ToastController(private val context: Context) {

    fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}