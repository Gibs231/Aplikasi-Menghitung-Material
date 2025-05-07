package com.gibraltar0123.materialapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gibraltar0123.materialapp.model.MaterialOption

@Database(entities = [MaterialOption::class], version = 1, exportSchema = false)
abstract class MaterialDb : RoomDatabase() {
    abstract fun materialDao(): MaterialDao

    abstract val dao: MaterialDao

    companion object {
        @Volatile
        private var INSTANCE: MaterialDb? = null

        fun getInstance(context: Context): MaterialDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MaterialDb::class.java,
                    "material_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
