package com.example.wheelify.ui.preview

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.wheelify.databinding.ActivityPreviewBinding

class PreviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreviewBinding

    companion object {
        const val EXTRA_IMG_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMG_URI))
        imageUri?.let {
            binding.resultImage.setImageURI(it)
        }

//        val golongan = intent.getStringExtra("golongan")
//        val confidence = intent.getDoubleExtra("confidence", 0.0)

        val textDetected = intent.getStringExtra(EXTRA_RESULT)
        binding.resultText.text = if (textDetected.isNullOrEmpty()) {
            "No result available"
        } else {
            textDetected
        }
    }
}
