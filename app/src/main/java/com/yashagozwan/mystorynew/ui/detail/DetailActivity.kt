package com.yashagozwan.mystorynew.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yashagozwan.mystorynew.R
import com.yashagozwan.mystorynew.databinding.ActivityDetailBinding
import com.yashagozwan.mystorynew.model.Story

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitleAppBar()
        renderDetailStory()
    }

    private fun setTitleAppBar() {
        supportActionBar?.title = getString(R.string.detail_story)
    }

    private fun renderDetailStory() {
        val story = intent.getParcelableExtra<Story>(STORY)
        if (story != null) {
            binding.apply {
                Glide.with(binding.root).load(story.photoUrl).apply(
                    RequestOptions.placeholderOf(R.drawable.image_loading)
                        .error(R.drawable.image_broken)
                ).into(binding.ivStoryImage)

                tvName.text = getString(R.string.name_concat, story.name)
                tvDescription.text = getString(R.string.description_concat, story.description)
            }
        }
    }

    companion object {
        const val STORY = "story"
    }
}