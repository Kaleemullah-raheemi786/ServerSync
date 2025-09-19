package com.example.demo.interfaces

import com.example.demo.network.model.AppItem

interface IAppRepository {
    suspend fun fetchApps(): Result<List<AppItem>>
}