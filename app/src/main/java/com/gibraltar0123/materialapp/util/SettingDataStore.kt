package com.gibraltar0123.materialapp.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "setting_preference"
)

class SettingDataStore(private val context: Context) {

    companion object {
        private val IS_LIST = booleanPreferencesKey("is_list")
        private val IS_YELLOW_THEME = booleanPreferencesKey("is_yellow_theme")
    }

    val layoutFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LIST] ?: true
    }

    val isYellowThemeFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_YELLOW_THEME] ?: false
    }

    suspend fun saveLayout(isList: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LIST] = isList
        }
    }

    suspend fun saveYellowTheme(isYellow: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_YELLOW_THEME] = isYellow
        }
    }
}
