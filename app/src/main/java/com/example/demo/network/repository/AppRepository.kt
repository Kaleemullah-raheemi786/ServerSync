package com.example.demo.network.repository

import android.util.Log
import com.example.demo.database.dao.AppDao
import com.example.demo.interfaces.IAppRepository
import com.example.demo.network.constants.NetworkConstants.BASE_URL
import com.example.demo.network.constants.NetworkConstants.LIST_APPS_ENDPOINT
import com.example.demo.network.model.AppItem
import com.example.demo.network.model.AppListResponse
import com.example.demo.network.utils.ApiError
import com.example.demo.network.utils.toAppItem
import com.example.demo.network.utils.toEntity
import com.example.demo.viewmodel.MainViewModel.Companion.TAG
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode

class AppRepository(
    private val client: HttpClient,
    private val appDao: AppDao
) : IAppRepository {


    /**
     * Fetches a list of apps from the remote API.
     *
     * If the network request succeeds and returns valid data, it stores the apps locally in the database.
     * In case of an error or exception, it attempts to load apps from the local database cache.
     *
     * @return Result containing either a list of AppItem on success, or an ApiError on failure.
     */
    override suspend fun fetchApps(): Result<List<AppItem>> {
        return try {
            // GET request to the API endpoint
            val response: HttpResponse = client.get("$BASE_URL/$LIST_APPS_ENDPOINT")

            Log.d(TAG, "Response Status: ${response.status}")

            when (response.status) {
                HttpStatusCode.OK -> {
                    // Parse the response body into AppListResponse model
                    val appListResponse = response.body<AppListResponse>()
                    Log.d(TAG, "Response Body: $appListResponse")

                    // Extract the list of apps from the response
                    val appItems = appListResponse.responses
                        .listApps
                        .datasets
                        .all
                        .data
                        .list

                    // If app list is not empty, cache it in the local DB and returning success
                    if (appItems.isNotEmpty()) {
                        appDao.clearApps() // Clear existing cached apps
                        appDao.insertApps(appItems.map { it.toEntity() }) // Cache new apps
                        Result.success(appItems)
                    } else {
                        // Handling case when API returns empty list
                        Log.e(TAG, "No app items found")
                        Result.failure(ApiError.UnknownError("No app items found"))
                    }
                }
                HttpStatusCode.BadRequest -> {
                    val errorMsg = response.bodyAsText().ifEmpty { "Invalid request data." }
                    Log.e(TAG, "BadRequest: $errorMsg")
                    Result.failure(ApiError.BadRequest(errorMsg))
                }

                HttpStatusCode.Forbidden -> {
                    val errorMsg = response.bodyAsText().ifEmpty { "Access denied: insufficient permissions." }
                    Log.e(TAG, "Forbidden: $errorMsg")
                    Result.failure(ApiError.Unauthorized(errorMsg))
                }

                HttpStatusCode.Conflict -> {
                    val errorMsg = response.bodyAsText().ifEmpty { "Conflict: operation could not be completed." }
                    Log.e(TAG, "Conflict: $errorMsg")
                    Result.failure(ApiError.Conflict(errorMsg))
                }

                HttpStatusCode.InternalServerError -> {
                    val errorMsg = response.bodyAsText().ifEmpty { "Server error. Please try again later." }
                    Log.e(TAG, "InternalServerError: $errorMsg")
                    Result.failure(ApiError.ServerError(errorMsg))
                }

                // Handle other HTTP errors by reading error body text
                else -> {
                    val errorMsg = response.bodyAsText().ifEmpty { "Unknown error" }
                    Log.e(TAG, "UnknownError: $errorMsg")
                    Result.failure(ApiError.UnknownError(errorMsg))
                }
            }

        } catch (e: Exception) {
            // Catch any exception (like no internet, timeout, etc.)
            Log.e(TAG, "Exception: ${e.localizedMessage}")

            // Attempt to fetch cached apps from the local DB
            val cachedApps = appDao.getAllAppsImmediate().map { it.toAppItem() }
            if (cachedApps.isNotEmpty()) {
                Log.d(TAG, "Fetched from local DB")
                Result.success(cachedApps)
            } else {
                // If no cached apps found, returning a failure result
                Result.failure(ApiError.UnknownError(e.localizedMessage ?: "Unknown exception"))
            }
        }
    }

}