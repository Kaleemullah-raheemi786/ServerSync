package com.example.appning.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appning.database.dao.AppDao
import com.example.appning.database.model.AppEntity
import com.example.appning.network.utils.Converters


@Database(entities = [AppEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}
