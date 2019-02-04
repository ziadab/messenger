package com.abouelfarah.messenger

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class NewMessage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"
    }
}
