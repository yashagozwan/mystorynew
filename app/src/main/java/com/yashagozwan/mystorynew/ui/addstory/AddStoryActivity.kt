package com.yashagozwan.mystorynew.ui.addstory

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.yashagozwan.mystorynew.R
import com.yashagozwan.mystorynew.databinding.ActivityAddStoryBinding
import com.yashagozwan.mystorynew.model.MyLatLon
import com.yashagozwan.mystorynew.repository.Result
import com.yashagozwan.mystorynew.ui.ViewModelFactory
import com.yashagozwan.mystorynew.ui.camera.CameraActivity
import com.yashagozwan.mystorynew.utils.Utils.reduceFileImage
import com.yashagozwan.mystorynew.utils.Utils.rotateBitmap
import com.yashagozwan.mystorynew.utils.Utils.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {
    private lateinit var binding: ActivityAddStoryBinding
    private val factory = ViewModelFactory.getInstance(this)
    private val addStoryViewModel: AddStoryViewModel by viewModels { factory }
    private var getFile: File? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var myLatLon: MyLatLon
    private lateinit var mMap: GoogleMap

    private val launcherIntentCameraX =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == CAMERA_X_RESULT) {
                val myFile = it.data?.getSerializableExtra("picture") as File
                val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
                getFile = myFile
                val result = rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBackCamera)
                binding.ivImagePreview.setImageBitmap(result)
            }
        }

    private val launcherIntentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImg = result.data?.data as Uri
                val myFile = uriToFile(selectedImg, this@AddStoryActivity)
                getFile = myFile
                binding.ivImagePreview.setImageURI(selectedImg)
                showToast("Gambar dipilih")
            }
        }

    private val launcherRequestPermissions =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyCurrentLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyCurrentLocation()
                }
                else -> {}
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitleAppBar()
        renderMap()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRE_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        setButtonListener()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun setTitleAppBar() {
        supportActionBar?.title = getString(R.string.add_story)
    }

    private fun setButtonListener() {
        binding.btnCamera.setOnClickListener(this)
        binding.btnGallery.setOnClickListener(this)
        binding.btnUpload.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_camera -> startCameraX()
            R.id.btn_gallery -> startGallery()
            R.id.btn_upload -> startUpload()
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.select_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun startUpload() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val description = binding.editDescription.text.toString().trim()
            val newDescription = description.toRequestBody("text/plain".toMediaTypeOrNull())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart =
                MultipartBody.Part.createFormData("photo", file.name, requestImageFile)

            if (description.isEmpty()) {
                binding.editDescription.error = getString(R.string.set_error_field)
                return
            }

            addStoryViewModel.getToken().observe(this) { token ->
                addStoryViewModel
                    .upload(
                        token,
                        imageMultipart,
                        newDescription,
                        myLatLon.latitude,
                        myLatLon.longitude
                    )
                    .observe(this) {
                        when (it) {
                            is Result.Loading -> {
                                binding.clLoading.visibility = View.VISIBLE
                                showToast("Loading")
                            }
                            is Result.Success -> {
                                binding.clLoading.visibility = View.GONE
                                finish()
                                showToast("Upload success")
                            }
                            is Result.Error -> {
                                binding.clLoading.visibility = View.GONE
                                showToast("Upload failed")
                            }
                        }
                    }
            }

        } else {
            showToast(getString(R.string.enter_picture_message))
        }
    }

    private fun allPermissionsGranted() = REQUIRE_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                finish()
            }
        }
    }

    private fun renderMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_main) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getMyCurrentLocation()

    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyCurrentLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                myLatLon = MyLatLon(it.latitude, it.longitude)
            }

        } else {
            launcherRequestPermissions.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            )
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRE_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}