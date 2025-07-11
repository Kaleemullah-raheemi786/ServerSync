package com.example.appning.interfaces

import com.example.appning.network.model.AppItem

interface IAppRepository {
    suspend fun fetchApps(): Result<List<AppItem>>
}