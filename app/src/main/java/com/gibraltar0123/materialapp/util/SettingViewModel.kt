package com.gibraltar0123.materialapp.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingViewModel(private val dataStore: SettingDataStore) : ViewModel() {



    val isYellowTheme = dataStore.isYellowThemeFlow.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), false
    )



    fun toggleYellowTheme() {
        viewModelScope.launch {
            dataStore.saveYellowTheme(!isYellowTheme.value)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class SettingViewModelFactory(private val dataStore: SettingDataStore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingViewModel(dataStore) as T
    }
}
