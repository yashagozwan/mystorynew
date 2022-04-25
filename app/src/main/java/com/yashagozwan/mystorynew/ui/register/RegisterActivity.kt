package com.yashagozwan.mystorynew.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.yashagozwan.mystorynew.R
import com.yashagozwan.mystorynew.databinding.ActivityRegisterBinding
import com.yashagozwan.mystorynew.model.Register
import com.yashagozwan.mystorynew.repository.Result
import com.yashagozwan.mystorynew.ui.ViewModelFactory
import com.yashagozwan.mystorynew.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding
    private val factory = ViewModelFactory.getInstance(this)
    private val registerViewModel: RegisterViewModel by viewModels { factory }
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitleAppBar()
        setButtonListener()
    }

    private fun setTitleAppBar() {
        supportActionBar?.title = getString(R.string.register)
    }

    private fun setButtonListener() {
        binding.tvShowPassword.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_show_password -> showPassword()
            R.id.btn_register -> register()
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

    private fun register() {
        val name = binding.editName.text.toString().trim()
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()

        if (name.isEmpty()) {
            binding.editName.error = getString(R.string.set_error_field)
            return
        }

        if (email.isEmpty()) {
            binding.editName.error = getString(R.string.set_error_field)
            return
        }

        if (password.isEmpty()) {
            binding.editName.error = getString(R.string.error_message)
            return
        }

        registerViewModel.register(Register(name, email, password)).observe(this) {
            when (it) {
                is Result.Loading -> {
                    binding.clLoading.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.clLoading.visibility = View.GONE
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                    showToast(getString(R.string.user_created))
                }
                is Result.Error -> {
                    binding.clLoading.visibility = View.GONE
                    showToast(getString(R.string.email_already_exist))
                }
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}