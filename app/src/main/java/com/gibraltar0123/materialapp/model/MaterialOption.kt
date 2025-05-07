package com.gibraltar0123.materialapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "material")
data class MaterialOption(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val pricePerPackage: Double,
    val stockPackage: Int,
    val imageResId: Int
)