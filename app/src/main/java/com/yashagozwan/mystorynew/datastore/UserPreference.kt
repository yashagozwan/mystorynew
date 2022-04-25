package com.yashagozwan.mystorynew.datastore

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

class UserPreference private constructor(private val context: Context) {

    suspend fun save(token: String) {
        context.dataStore.edit {
            it[TOKEN] = token
        }
    }

    fun token(): Flow<String> = context.dataStore.data.map { it[TOKEN] ?: "" }

    suspend fun delete() {
        context.dataStore.edit {
            it[TOKEN] = ""
        }
    }

    companion object {
        private val TOKEN = stringPreferencesKey("token")

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: UserPreference? = null
        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: UserPreference(context)
        }.also { instance = it }
    }
}