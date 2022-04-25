package com.yashagozwan.mystorynew.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.activity.viewModels
import com.yashagozwan.mystorynew.R
import com.yashagozwan.mystorynew.databinding.ActivityLoginBinding
import com.yashagozwan.mystorynew.model.Login
import com.yashagozwan.mystorynew.repository.Result
import com.yashagozwan.mystorynew.ui.ViewModelFactory
import com.yashagozwan.mystorynew.ui.main.MainActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private val factory = ViewModelFactory.getInstance(this)
    private val loginViewModel: LoginViewModel by viewModels { factory }
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        redirect()
        setButtonListener()
    }

    private fun redirect() {
        loginViewModel.getToken().observe(this) {
            if (it.isNotEmpty()) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun setButtonListener() {
        binding.tvShowPassword.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_show_password -> showPassword()
            R.id.btn_login -> login()
        }
    }

    private fun showPassword() {
        binding.tvShowPassword.text = when (isPasswordVisible) {
            true -> getString(R.string.show_password)
            false -> getString(R.string.hide_password)
        }

        binding.editPassword.transformationMethod = when (isPasswordVisible) {
            true -> PasswordTransformationMethod()
            false -> HideReturnsTransformationMethod.getInstance()
        }

        isPasswordVisible = !isPasswordVisible
    }

    private fun login() {
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()

        if (email.isEmpty()) {
            binding.editEmail.error = getString(R.string.set_error_field)
            return
        }

        if (password.isEmpty()) {
            binding.editPassword.error = getString(R.string.set_error_field)
            return
        }

        loginViewModel.login(Login(email, password)).observe(this) {
            when (it) {
                is Result.Loading -> {
                    binding.clLoading.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.clLoading.visibility = View.GONE
                    loginViewModel.saveToken(it.data.loginResult.token)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
                is Result.Error -> {
                    binding.clLoading.visibility = View.GONE
                }
            }
        }
    }
}