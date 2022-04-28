package com.yashagozwan.mystorynew.ui.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yashagozwan.mystorynew.R
import com.yashagozwan.mystorynew.databinding.ActivityMapBinding
import com.yashagozwan.mystorynew.repository.Result
import com.yashagozwan.mystorynew.ui.ViewModelFactory
import kotlin.properties.Delegates

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapBinding
    private lateinit var mMap: GoogleMap
    private val factory = ViewModelFactory.getInstance(this)
    private val mapViewModel: MapViewModel by viewModels { factory }
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideAppBar()
        renderMap()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun hideAppBar() {
        supportActionBar?.hide()
    }

    private fun renderMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        mapViewModel.getToken().observe(this) { token ->
            mapViewModel.storiesAndLocation(token).observe(this) {
                when (it) {
                    is Result.Loading -> {
                        showToast("Loading")
                    }
                    is Result.Success -> {
                        showToast("Success")
                        for (story in it.data) {
                            val latLon = LatLng(story.lat, story.lon)
                            mMap.addMarker(MarkerOptions().position(latLon).title(story.name))
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLon, 10F))
                        }
                    }
                    is Result.Error -> {
                        showToast("Error")
                        finish()
                    }
                }
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}