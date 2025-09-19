package com.example.demo.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.demo.database.dao.AppDao
import com.example.demo.database.model.AppEntity
import com.example.demo.network.utils.Converters


@Database(entities = [AppEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}
