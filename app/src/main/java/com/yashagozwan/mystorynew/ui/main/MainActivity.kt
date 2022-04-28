package com.yashagozwan.mystorynew.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.yashagozwan.mystorynew.R
import com.yashagozwan.mystorynew.databinding.ActivityMainBinding
import com.yashagozwan.mystorynew.repository.Result
import com.yashagozwan.mystorynew.ui.ViewModelFactory
import com.yashagozwan.mystorynew.ui.addstory.AddStoryActivity
import com.yashagozwan.mystorynew.ui.map.MapActivity
import com.yashagozwan.mystorynew.ui.start.StartActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private val factory = ViewModelFactory.getInstance(this)
    private val mainViewModel: MainViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitleAppBar()
        setButtonListener()
        renderListStory()
    }

    private fun setTitleAppBar() {
        supportActionBar?.title = getString(R.string.story)
    }

    private fun renderListStory() {
        val storyAdapter = StoryAdapter()
        val rvStory = binding.rvStory
        rvStory.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter { storyAdapter.retry() }
        )
        rvStory.layoutManager = LinearLayoutManager(this)
        mainViewModel.getToken().observe(this) { token ->
            mainViewModel.getStories(token).observe(this) {
                storyAdapter.submitData(lifecycle, it)
            }
        }
    }

    private fun setButtonListener() {
        binding.btnAddStory.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_add_story -> startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout_menu -> showAlertDialog()
            R.id.change_language_menu -> setLanguage()
            R.id.map_menu -> startActivity(Intent(this, MapActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setLanguage() {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setTitle(R.string.logout)
            .setMessage(R.string.are_you_sure)
            .setCancelable(false)
            .setPositiveButton(R.string.yes) { _, _ -> logout() }
            .setNegativeButton(R.string.no) { dialog, _ -> dialog.cancel() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }

    private fun logout() {
        mainViewModel.deleteToken()
        startActivity(Intent(this, StartActivity::class.java))
        finish()
    }
}