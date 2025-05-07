package com.gibraltar0123.materialapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gibraltar0123.materialapp.model.MaterialOption
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterial(materialOption: MaterialOption)

    @Update
    suspend fun update(material: MaterialOption)

    @Query("SELECT * FROM material ORDER BY name ASC")
    fun getAllMaterials(): Flow<List<MaterialOption>>
}
