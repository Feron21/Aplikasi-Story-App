package com.example.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.R
import com.example.storyapp.adapter.StoryAdapter
import com.example.storyapp.data.model.StoryItem
import com.example.storyapp.ui.addstory.AddStoryActivity
import com.example.storyapp.ui.auth.LoginActivity
import com.example.storyapp.ui.detail.DetailActivity
import com.example.storyapp.ui.maps.MapsActivity
import com.example.storyapp.ui.viewModel.MainViewModel
import com.example.storyapp.utils.Injection
import com.example.storyapp.utils.SharedPrefs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var storyAdapter: StoryAdapter
    private var token: String? = null

    private val addStoryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            storyAdapter.refresh() // Refresh the story list after adding a story
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()  // Set up the toolbar
        checkToken()  // Check if the user has a valid token
        setupRecyclerView()  // Set up the RecyclerView to display stories
        setupAddStoryButton()  // Set up the "Add Story" button

        // Launch coroutine to initialize ViewModel with repository
        lifecycleScope.launch {
            // Use 'provideRepository' inside a coroutine
            val repository = Injection.provideRepository(this@MainActivity)
            viewModel = MainViewModel(repository)

            observeStories() // Observe the stories from the ViewModel
        }
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)  // Set up the toolbar
    }

    private fun checkToken() {
        val sharedPrefs = SharedPrefs(this)
        token = sharedPrefs.getToken()

        // If there's no valid token, navigate to the LoginActivity
        if (token.isNullOrEmpty()) {
            navigateToLogin()
        }
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.rv_story)
        storyAdapter = StoryAdapter { storyItem -> navigateToDetail(storyItem) }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupAddStoryButton() {
        findViewById<FloatingActionButton>(R.id.fab_add_story).setOnClickListener {
            navigateToAddStory()  // Navigate to the AddStoryActivity when the button is clicked
        }
    }

    private fun observeStories() {
        lifecycleScope.launch {
            token?.let { safeToken ->
                // Fetch all stories from ViewModel using the provided token
                viewModel.getAllStories(safeToken).collectLatest { pagingData ->
                    storyAdapter.submitData(pagingData)  // Update the adapter with new stories
                }
            }
        }
    }

    // Update menu creation for additional functionalities
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)  // Inflate the menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout() // Logout functionality
                true
            }
            R.id.action_maps -> {
                navigateToMaps() // Navigate to MapsActivity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToMaps() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent) // Start MapsActivity
    }

    private fun navigateToAddStory() {
        val intent = Intent(this, AddStoryActivity::class.java).apply {
            putExtra("TOKEN", token)  // Pass the token to the AddStoryActivity
        }
        addStoryLauncher.launch(intent)  // Launch AddStoryActivity
    }

    private fun navigateToDetail(storyItem: StoryItem) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_STORY, storyItem)  // Pass the story item to the DetailActivity
        }
        startActivity(intent)  // Start the DetailActivity
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()  // Finish MainActivity after navigating to LoginActivity
    }

    private fun logout() {
        SharedPrefs(this).clearToken()  // Clear the token from SharedPrefs
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
        navigateToLogin()  // Navigate to LoginActivity after logout
    }
}
