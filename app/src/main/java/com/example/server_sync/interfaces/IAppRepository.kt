package com.example.server_sync.interfaces

import com.example.server_sync.network.model.AppItem

interface IAppRepository {
    suspend fun fetchApps(): Result<List<AppItem>>
}