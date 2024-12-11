package com.example.wheelify.ui.about

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.wheelify.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_about)

        val backButton = findViewById<ImageButton>(R.id.btn_back)
        backButton.setOnClickListener {
           onBackPressedDispatcher
               .onBackPressed()
        }


    }
}