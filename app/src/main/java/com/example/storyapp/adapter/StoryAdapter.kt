package com.example.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.model.StoryItem
import com.example.storyapp.databinding.ItemStoryBinding

class StoryAdapter(private val onClick: (StoryItem) -> Unit) :
    PagingDataAdapter<StoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StoryViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        story?.let { holder.bind(it) }
    }

    suspend fun refreshData() {
        submitData(PagingData.empty())  // Reset data, forcing refresh
    }

    class StoryViewHolder(
        private val binding: ItemStoryBinding,
        private val onClick: (StoryItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(story: StoryItem) {
            binding.tvStoryName.text = story.name
            Glide.with(binding.ivItemPhoto.context)
                .load(story.photoUrl)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.error_image)
                .into(binding.ivItemPhoto)

            binding.root.setOnClickListener {
                onClick(story)
            }
        }
    }

    companion object {
        internal val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
