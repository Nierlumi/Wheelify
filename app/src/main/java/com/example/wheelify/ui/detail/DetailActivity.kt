package com.example.wheelify.ui.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.wheelify.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        val id = intent.getIntExtra("id", 0)
        detailViewModel.getDetailNews(id)
        detailViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        detailViewModel.news.observe(this) { news ->
            if (news != null) {
                binding.tvItemNameDetail.text = news.title
                binding.tvDescription.text = news.description
                Glide.with(this)
                    .load(news.image)
                    .into(binding.imgItemPhoto)

            }
        }
    }
}