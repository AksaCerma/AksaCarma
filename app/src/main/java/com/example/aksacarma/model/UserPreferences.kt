package com.example.aksacarma.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    // Home
    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                username = preferences[USERNAME_KEY] ?:"",
                name = preferences[NAME_KEY] ?:"",
                token = preferences[TOKEN_KEY] ?:"",
                isLogin = preferences[STATE_KEY] ?: false
            )
        }
    }

    //Login
    suspend fun getLoginUser(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = user.username
            preferences[NAME_KEY] = user.name
            preferences[TOKEN_KEY] = user.token
            preferences[STATE_KEY] = user.isLogin
        }
    }

    suspend fun logoutUser() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun getToken() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val USERNAME_KEY = stringPreferencesKey("username")
        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>) : UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}