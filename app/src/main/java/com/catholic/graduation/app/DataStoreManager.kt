package com.catholic.graduation.app

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import com.catholic.graduation.presentation.util.Constants.AUTO_LOGIN
import com.catholic.graduation.presentation.util.Constants.X_ACCESS_TOKEN
import com.catholic.graduation.presentation.util.Constants.X_REFRESH_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey(X_ACCESS_TOKEN)
        private val REFRESH_TOKEN_KEY = stringPreferencesKey(X_REFRESH_TOKEN)
        private val AUTO_LOGIN_KEY = booleanPreferencesKey(AUTO_LOGIN)
    }

    fun getAccessToken(): Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN_KEY] ?: ""
        }
    }

    fun getRefreshToken() : Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[REFRESH_TOKEN_KEY] ?: ""
        }
    }

    fun getAutoLogin() : Flow<Boolean?> {
        return dataStore.data.map { prefs ->
            prefs[AUTO_LOGIN_KEY] ?: false
        }
    }

    suspend fun putAccessToken(token : String){
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = token
        }
    }

    suspend fun putRefreshToken(token : String){
        dataStore.edit { prefs ->
            prefs[REFRESH_TOKEN_KEY] = token
        }
    }

    suspend fun putAutoLogin(isAuto : Boolean){
        dataStore.edit { prefs ->
            prefs[AUTO_LOGIN_KEY] = isAuto
        }
    }

    suspend fun deleteAccessToken(){
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
        }
    }

    suspend fun deleteRefreshToken(){
        dataStore.edit { prefs ->
            prefs.remove(REFRESH_TOKEN_KEY)
        }
    }

    suspend fun deleteAutoLogin(){
        dataStore.edit { prefs ->
            prefs.remove(AUTO_LOGIN_KEY)
        }
    }

}