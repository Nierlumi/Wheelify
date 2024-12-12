package com.example.wheelify.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.wheelify.R
import com.example.wheelify.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher
                .onBackPressed()
        }


        val id = intent.getIntExtra("id", 0)
        detailViewModel.getDetailNews(id)
        detailViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        detailViewModel.news.observe(this) { news ->
            if (news != null) {
                binding.tvItemNameDetail.text = news.article?.title
                binding.tvDescription.text = news.article?.description
                Glide.with(this)
                    .load(news.article?.image)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_image_not_found)
                    .into(binding.imgItemPhoto)

                binding.btnSource.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = android.net.Uri.parse(news.article?.url)
                    startActivity(intent)
                }


            }
        }


    }
}