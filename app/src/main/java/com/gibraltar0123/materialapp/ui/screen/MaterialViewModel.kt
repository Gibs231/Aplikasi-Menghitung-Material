package com.gibraltar0123.materialapp.ui.screen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibraltar0123.materialapp.database.MaterialDao
import com.gibraltar0123.materialapp.model.MaterialOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MaterialViewModel(private val dao: MaterialDao) : ViewModel() {


    val allMaterials: Flow<List<MaterialOption>> = dao.getAllMaterials()

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
}
