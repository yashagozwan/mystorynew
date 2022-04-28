package com.yashagozwan.mystorynew.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yashagozwan.mystorynew.model.Story

@Database(
    entities = [Story::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao

    companion object {
        @Volatile
        private var instance: StoryDatabase? = null

        @JvmStatic
        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context,
                StoryDatabase::class.java, "story_database"
            ).fallbackToDestructiveMigration().build().also { instance = it }
        }
    }
}