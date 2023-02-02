package com.creations.bawender.sample

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.creations.bawender.kservicelocator.R
import com.creations.bawender.service_locator.helpers.serviceViewModel

class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by serviceViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)


        findViewById<Button>(R.id.hint_button)?.setOnClickListener {
            viewModel.showRandomMsg()
        }
    }

}