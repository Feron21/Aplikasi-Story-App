package com.example.storyapp.ui.detail

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.model.StoryItem

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Get the StoryItem object passed from the com.example.storyapp.ui.main.MainActivity
        val story = intent.getParcelableExtra<StoryItem>(EXTRA_STORY)

        // Bind the data to the views if the StoryItem is not null
        story?.let {
            // Find the TextViews by ID
            val storyNameTextView: TextView = findViewById(R.id.tv_item_name)
            val storyDescriptionTextView: TextView = findViewById(R.id.tv_item_description)
            val storyImageView: ImageView = findViewById(R.id.iv_item_photo)

            // Set the values from StoryItem to the TextViews
            storyNameTextView.text = it.name  // Assuming 'name' is one of the fields in StoryItem
            storyDescriptionTextView.text = it.description  // Assuming 'description' is one of the fields

            // Load the image (if available) using Glide (or you can use Picasso)
            Glide.with(this)
                .load(it.photoUrl)  // Assuming 'photoUrl' is one of the fields in StoryItem
                .placeholder(R.drawable.ic_image_placeholder)
                .into(storyImageView)
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}
