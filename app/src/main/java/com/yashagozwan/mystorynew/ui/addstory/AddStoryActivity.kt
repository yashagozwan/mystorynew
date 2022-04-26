package com.yashagozwan.mystorynew.ui.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.yashagozwan.mystorynew.R
import com.yashagozwan.mystorynew.databinding.ActivityAddStoryBinding
import com.yashagozwan.mystorynew.ui.ViewModelFactory
import com.yashagozwan.mystorynew.ui.camera.CameraActivity

class AddStoryActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAddStoryBinding
    private val factory = ViewModelFactory.getInstance(this)
    private val addStoryViewModel: AddStoryViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRE_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        setButtonListener()
    }

    private fun setButtonListener() {
        binding.btnCamera.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_camera -> startCameraX()
        }
    }

    private fun startCameraX() {
        Intent(this, CameraActivity::class.java).also {
            startActivity(it)
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
                showToast("Tidak mendapatkan permission")
                finish()
            }
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