package com.yashagozwan.mystorynew.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yashagozwan.mystorynew.databinding.StoryItemBinding
import com.yashagozwan.mystorynew.model.Story

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
                Glide.with(binding.root).load(story.photoUrl).into(binding.ivPhoto)
                tvName.text = story.name
            }
        }
    }

}