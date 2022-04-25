package com.yashagozwan.mystorynew.ui.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.yashagozwan.mystorynew.R
import com.yashagozwan.mystorynew.databinding.ActivityStartBinding
import com.yashagozwan.mystorynew.ui.ViewModelFactory
import com.yashagozwan.mystorynew.ui.login.LoginActivity
import com.yashagozwan.mystorynew.ui.main.MainActivity

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
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_login -> startActivity(Intent(this, LoginActivity::class.java))
            R.id.btn_register -> {}
        }
    }
}