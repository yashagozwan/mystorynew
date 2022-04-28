package com.yashagozwan.mystorynew.ui.start

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.viewModels
import com.yashagozwan.mystorynew.R
import com.yashagozwan.mystorynew.databinding.ActivityStartBinding
import com.yashagozwan.mystorynew.ui.ViewModelFactory
import com.yashagozwan.mystorynew.ui.login.LoginActivity
import com.yashagozwan.mystorynew.ui.main.MainActivity
import com.yashagozwan.mystorynew.ui.register.RegisterActivity

class StartActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityStartBinding
    private val factory = ViewModelFactory.getInstance(this)
    private val startViewModel: StartViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideAppBar()
        redirect()
        setButtonListener()
        setAnimation()
    }

    private fun redirect() {
        startViewModel.getToken().observe(this) {
            if (it.isNotEmpty()) {
                startActivity(Intent(this@StartActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun hideAppBar() {
        supportActionBar?.hide()
    }

    private fun setButtonListener() {
        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        binding.btnChangeLanguage.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_login -> startActivity(Intent(this, LoginActivity::class.java))
            R.id.btn_register -> startActivity(Intent(this, RegisterActivity::class.java))
            R.id.btn_change_language -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun setAnimation() {
        val positionStart = -30F
        val positionEnd = 30F
        val myDuration = 1000L

        val illustrationPosition = ObjectAnimator.ofFloat(
            binding.ivIllustrationGoto,
            View.TRANSLATION_Y,
            -500F,
            0F
        ).setDuration(myDuration)

        val illustrationLoop = ObjectAnimator.ofFloat(
            binding.ivIllustrationGoto,
            View.TRANSLATION_X,
            positionStart,
            positionEnd
        ).apply {
            duration = myDuration
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
        }

        AnimatorSet().apply {
            playTogether(illustrationPosition, illustrationLoop)
        }.start()

        val btnLogin =
            ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1F).setDuration(myDuration)

        val btnRegister =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1F).setDuration(myDuration)

        val btnLanguage =
            ObjectAnimator.ofFloat(binding.btnChangeLanguage, View.ALPHA, 1F)
                .setDuration(myDuration)

        AnimatorSet().apply {
            playSequentially(
                btnLogin,
                btnRegister,
                btnLanguage
            )
        }.start()
    }
}