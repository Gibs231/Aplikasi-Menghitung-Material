package com.gibraltar0123.materialapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibraltar0123.materialapp.database.MaterialDao
import com.gibraltar0123.materialapp.model.CheckoutItem
import com.gibraltar0123.materialapp.model.MaterialOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MaterialViewModel(private val dao: MaterialDao) : ViewModel() {

    val allMaterials: Flow<List<MaterialOption>> = dao.getAllMaterials()

    // StateFlow for checkout items
    private val _checkoutItems = MutableStateFlow<List<CheckoutItem>>(emptyList())
    val checkoutItems: StateFlow<List<CheckoutItem>> = _checkoutItems.asStateFlow()

    // Insert data material
    fun insert(name: String, pricePerPackage: Double, stockPackage: Int, imageResId: Int) {
        val material = MaterialOption(
            name = name,
            pricePerPackage = pricePerPackage,
            stockPackage = stockPackage,
            imageResId = imageResId
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insertMaterial(material)
        }
    }

    // Update data material
    fun update(id: Long, name: String, pricePerPackage: Double, stockPackage: Int, imageResId: Int) {
        val material = MaterialOption(
            id = id,
            name = name,
            pricePerPackage = pricePerPackage,
            stockPackage = stockPackage,
            imageResId = imageResId
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(material)
        }
    }

    // Delete data material
    fun delete(material: MaterialOption) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(material)
        }
    }

    suspend fun getMaterialById(id: Long): MaterialOption? {
        return dao.getMaterialById(id)
    }

    // Add items to checkout
    fun addToCheckout(materials: List<MaterialOption>, quantities: List<Int>) {
        val newCheckoutItems = materials.mapIndexed { index, material ->
            if (quantities[index] > 0) {
                CheckoutItem(
                    id = material.id,
                    name = material.name,
                    pricePerPackage = material.pricePerPackage,
                    quantity = quantities[index],
                    imageResId = material.imageResId
                )
            } else null
        }.filterNotNull()

        val currentList = _checkoutItems.value.toMutableList()

        // Update quantities if item already exists, otherwise add new item
        newCheckoutItems.forEach { newItem ->
            val existingItemIndex = currentList.indexOfFirst { it.id == newItem.id }
            if (existingItemIndex != -1) {
                // Update existing item
                val existingItem = currentList[existingItemIndex]
                currentList[existingItemIndex] = existingItem.copy(quantity = existingItem.quantity + newItem.quantity)
            } else {
                // Add new item
                currentList.add(newItem)
            }
        }

        _checkoutItems.value = currentList
    }

    // Remove item from checkout
    fun removeCheckoutItem(item: CheckoutItem) {
        _checkoutItems.value = _checkoutItems.value.filter { it.id != item.id }
    }

    // Clear all checkout items
    fun clearCheckout() {
        _checkoutItems.value = emptyList()
    }
}