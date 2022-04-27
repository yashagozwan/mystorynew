package com.yashagozwan.mystorynew.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yashagozwan.mystorynew.di.Injection
import com.yashagozwan.mystorynew.repository.StoryRepository
import com.yashagozwan.mystorynew.ui.addstory.AddStoryViewModel
import com.yashagozwan.mystorynew.ui.login.LoginViewModel
import com.yashagozwan.mystorynew.ui.main.MainViewModel
import com.yashagozwan.mystorynew.ui.map.MapViewModel
import com.yashagozwan.mystorynew.ui.register.RegisterViewModel
import com.yashagozwan.mystorynew.ui.splash.SplashViewModel
import com.yashagozwan.mystorynew.ui.start.StartViewModel

class ViewModelFactory private constructor(private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(
                storyRepository
            ) as T

            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel(
                storyRepository
            ) as T

            modelClass.isAssignableFrom(StartViewModel::class.java) -> StartViewModel(
                storyRepository
            ) as T

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(
                storyRepository
            ) as T

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(
                storyRepository
            ) as T

            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> AddStoryViewModel(
                storyRepository
            ) as T

            modelClass.isAssignableFrom(MapViewModel::class.java) -> MapViewModel(
                storyRepository
            ) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }


    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(Injection.provideRepository(context))
        }.also { instance = it }
    }
}