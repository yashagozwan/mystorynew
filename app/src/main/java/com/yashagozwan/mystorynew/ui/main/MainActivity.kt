package com.yashagozwan.mystorynew.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.yashagozwan.mystorynew.R
import com.yashagozwan.mystorynew.databinding.ActivityMainBinding
import com.yashagozwan.mystorynew.ui.ViewModelFactory
import com.yashagozwan.mystorynew.ui.start.StartActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val factory = ViewModelFactory.getInstance(this)
    private val mainViewModel: MainViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout_menu -> showAlertDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure ?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                logout()
            }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }

    private fun logout() {
        mainViewModel.deleteToken()
        startActivity(Intent(this, StartActivity::class.java))
        finish()
    }
}