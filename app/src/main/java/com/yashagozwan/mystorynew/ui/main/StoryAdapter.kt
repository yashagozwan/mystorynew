package com.yashagozwan.mystorynew.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yashagozwan.mystorynew.R
import com.yashagozwan.mystorynew.databinding.StoryItemBinding
import com.yashagozwan.mystorynew.model.Story
import com.yashagozwan.mystorynew.ui.detail.DetailActivity

class StoryAdapter(private val listStory: List<Story>) :
    RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = listStory[position]
        holder.bind(story)
    }

    override fun getItemCount() = listStory.size

    inner class ViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.apply {
                Glide.with(binding.root).load(story.photoUrl).apply(
                    RequestOptions
                        .placeholderOf(R.drawable.image_loading)
                        .error(R.drawable.image_broken)
                ).into(binding.ivPhoto)
                tvName.text = story.name
                cvStoryItem.setOnClickListener {
                    val intent = Intent(it.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.STORY, story)
                    it.context.startActivity(intent)
                }
            }
        }
    }

}