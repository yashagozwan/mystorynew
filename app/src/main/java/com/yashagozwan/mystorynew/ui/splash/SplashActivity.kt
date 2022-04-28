package com.yashagozwan.mystorynew.ui.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.yashagozwan.mystorynew.databinding.ActivitySplashBinding
import com.yashagozwan.mystorynew.ui.ViewModelFactory
import com.yashagozwan.mystorynew.ui.main.MainActivity
import com.yashagozwan.mystorynew.ui.start.StartActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var factory: ViewModelFactory
    private val splashViewModel: SplashViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = ViewModelFactory.getInstance(this)
        hideAppBar()
        redirect()
        setAnimation()
        setHideSystemUI()
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

    private fun setAnimation() {
        val viewOpacity = 1F
        val oneSecond = 1000L
        val startY = -600F
        val endY = 0F

        val illustrationOpacity = ObjectAnimator
            .ofFloat(binding.ivIllustrationStory, View.ALPHA, viewOpacity)
            .setDuration(oneSecond)

        val captionOpacity = ObjectAnimator
            .ofFloat(binding.tvCaption, View.ALPHA, viewOpacity)
            .setDuration(oneSecond)

        val illustrationY = ObjectAnimator
            .ofFloat(binding.ivIllustrationStory, View.TRANSLATION_Y, startY, endY)
            .setDuration(oneSecond)

        val captionY = ObjectAnimator
            .ofFloat(binding.tvCaption, View.TRANSLATION_Y, startY, endY)
            .setDuration(oneSecond)


        AnimatorSet().apply {
            playSequentially(
                AnimatorSet().apply {
                    playTogether(illustrationOpacity, illustrationY)
                },
                AnimatorSet().apply {
                    playTogether(captionOpacity, captionY)
                }
            )
        }.start()
    }

    private fun setHideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window?.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
            )
        }
    }
}