package com.yashagozwan.mystorynew.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.yashagozwan.mystorynew.databinding.ActivitySplashBinding
import com.yashagozwan.mystorynew.ui.ViewModelFactory
import com.yashagozwan.mystorynew.ui.main.MainActivity
import com.yashagozwan.mystorynew.ui.start.StartActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val factory = ViewModelFactory.getInstance(this)
    private val splashViewModel: SplashViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideAppBar()
        redirect()
    }

    private fun hideAppBar() {
        supportActionBar?.hide()
    }

    private fun redirect() {
        val duration = 3000L
        Handler(Looper.getMainLooper()).postDelayed({
            splashViewModel.getToken().observe(this) {
                val intent = when (it.isNotEmpty()) {
                    true -> Intent(this, MainActivity::class.java)
                    false -> Intent(this, StartActivity::class.java)
                }
                startActivity(intent)
                finish()
            }
        }, duration)
    }
}