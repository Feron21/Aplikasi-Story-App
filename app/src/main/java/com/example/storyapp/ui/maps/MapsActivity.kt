package com.example.storyapp.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.data.model.StoryItem
import com.example.storyapp.databinding.ActivityMapsBinding
import com.example.storyapp.ui.viewmodel.MapsViewModel
import com.example.storyapp.ui.viewmodel.ViewModelFactory
import com.example.storyapp.utils.SharedPrefs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var sharedPrefs: SharedPrefs
    private lateinit var mapsViewModel: MapsViewModel

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
        private const val DEFAULT_ZOOM = 10f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPrefs
        sharedPrefs = SharedPrefs(this)

        // Initialize ViewModel
        initializeViewModel()

        // Set up the map fragment
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initializeViewModel() {
        mapsViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(application)
        )[MapsViewModel::class.java]
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        enableLocation()

        // Observe and display stories with location data
        observeStories()
    }

    private fun enableLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private fun observeStories() {
        val token = sharedPrefs.getToken()
        if (token.isNullOrEmpty()) {
            Log.e("MapsActivity", "Token is missing or empty")
            Toast.makeText(this, "User is not logged in. Please log in to continue.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        mapsViewModel.fetchStoriesWithLocation("Bearer $token")

        mapsViewModel.stories.observe(this) { stories ->
            if (stories.isNotEmpty()) {
                addStoryMarkers(stories)
            } else {
                Toast.makeText(this, "No stories available with location data.", Toast.LENGTH_SHORT).show()
            }
        }

        mapsViewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun addStoryMarkers(stories: List<StoryItem>) {
        stories.forEach { story ->
            val lat = story.lat ?: return@forEach
            val lon = story.lon ?: return@forEach
            val latLng = LatLng(lat, lon)
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(story.name)
                    .snippet(story.description)
            )
        }

        // Move the camera to the first story location if available
        stories.firstOrNull()?.let {
            val firstStoryLatLng = LatLng(it.lat ?: 0.0, it.lon ?: 0.0)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstStoryLatLng, DEFAULT_ZOOM))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocation()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
