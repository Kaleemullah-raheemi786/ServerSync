package com.example.appning.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.appning.database.model.AppEntity

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApps(apps: List<AppEntity>)

    @Query("SELECT * FROM apps")
    fun getAllApps(): LiveData<List<AppEntity>>

    @Query("SELECT * FROM apps")
    suspend fun getAllAppsImmediate(): List<AppEntity>

    @Query("DELETE FROM apps")
    suspend fun clearApps()
}


