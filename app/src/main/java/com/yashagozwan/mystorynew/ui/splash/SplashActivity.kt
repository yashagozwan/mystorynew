package com.yashagozwan.mystorynew.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.yashagozwan.mystorynew.databinding.ActivitySplashBinding
import com.yashagozwan.mystorynew.ui.ViewModelFactory

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val factory = ViewModelFactory.getInstance(this)
    private val splashViewModel: SplashViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}