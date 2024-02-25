package com.main.chatter.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Based on DataStore and Dependency Injection by Android Developers
 * https://medium.com/androiddevelopers/datastore-and-dependency-injection-ea32b95704e3
 * Used to store the current user and keep them logged in
 */
class UserDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val userName = stringPreferencesKey("userName")
    private val passWord = stringPreferencesKey("passWord")
    private val loggedIn = booleanPreferencesKey("loggedIn")

    fun getUser(): Flow<Triple<String, String, Boolean>> {
        return dataStore.data.map { preferences ->
            val userName: String = preferences[userName] ?: ""
            val passWord: String = preferences[passWord] ?: ""
            val loggedIn: Boolean = preferences[loggedIn] ?: false
            Triple(userName, passWord, loggedIn)
        }
    }

    suspend fun updateUser(userName: String, passWord: String, loggedIn: Boolean) {
        this.dataStore.edit { preferences ->
            preferences[this.userName] = userName
            preferences[this.passWord] = passWord
            preferences[this.loggedIn] = loggedIn
        }
    }
}