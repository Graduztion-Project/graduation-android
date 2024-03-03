package com.catholic.graduation.app

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp
import androidx.datastore.preferences.core.Preferences
import com.catholic.graduation.presentation.util.Constants.APP_NAME

@HiltAndroidApp
class App : Application() {

    init {
        instance = this
    }

    companion object {
        lateinit var instance : App
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = APP_NAME)
        fun getContext(): Context = instance.applicationContext
    }

}