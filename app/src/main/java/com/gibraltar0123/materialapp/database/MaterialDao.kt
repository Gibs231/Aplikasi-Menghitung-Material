package com.gibraltar0123.materialapp.database

import androidx.room.Dao
import androidx.room.Delete
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

    @Delete
    suspend fun delete(material: MaterialOption)

    @Query("SELECT * FROM material WHERE id = :id LIMIT 1")
    suspend fun getMaterialById(id: Long): MaterialOption?

    @Query("SELECT * FROM material")
    fun getAllMaterials(): Flow<List<MaterialOption>>  // Tambahkan query ini
}
