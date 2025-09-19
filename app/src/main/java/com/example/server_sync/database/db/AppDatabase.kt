package com.example.server_sync.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.server_sync.database.dao.AppDao
import com.example.server_sync.database.model.AppEntity
import com.example.server_sync.network.utils.Converters


@Database(entities = [AppEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}
