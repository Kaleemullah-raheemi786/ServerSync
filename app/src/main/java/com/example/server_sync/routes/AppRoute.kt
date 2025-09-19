package com.example.server_sync.routes

import kotlinx.serialization.Serializable

@Serializable
object AppsListScreenRoute

@Serializable
data class AppDetailsScreenRoute(
    val appName: String,
    val iconUrl: String,
    val rating: Float
)