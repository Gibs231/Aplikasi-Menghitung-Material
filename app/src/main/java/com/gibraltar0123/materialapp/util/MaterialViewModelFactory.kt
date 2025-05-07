package com.gibraltar0123.materialapp.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gibraltar0123.materialapp.database.MaterialDb
import com.gibraltar0123.materialapp.ui.screen.MaterialViewModel

class MaterialViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = MaterialDb.getInstance(context).materialDao()
        if (modelClass.isAssignableFrom(MaterialViewModel::class.java)) {
            return MaterialViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
