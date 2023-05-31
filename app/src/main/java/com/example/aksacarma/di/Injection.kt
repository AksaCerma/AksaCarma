package com.example.aksacarma.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.aksacarma.data.remote.retrofit.ApiConfig
import com.example.aksacarma.model.UserPreferences
import com.example.aksacarma.repository.UserRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("token")

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = UserPreferences.getInstance(context.dataStore)
        val preferences = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, preferences)
    }
}