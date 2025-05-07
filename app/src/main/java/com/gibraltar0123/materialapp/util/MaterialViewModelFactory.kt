package com.gibraltar0123.materialapp.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gibraltar0123.materialapp.database.MaterialDb
import com.gibraltar0123.materialapp.viewmodel.MaterialViewModel

class MaterialViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MaterialViewModel::class.java)) {
            val dao = MaterialDb.getInstance(context).materialDao()
            @Suppress("UNCHECKED_CAST")
            return MaterialViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}