package com.example.appning.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appning.network.utils.ApiError
import com.example.appning.repository.AppRepository
import com.example.appning.state.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val appRepository: AppRepository
): ViewModel() {

    companion object {
        const val TAG = "MainViewModel"
    }

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()


    fun showLoading() {
        _uiState.update { it.copy(isLoading = true) }
    }

    fun showError(message: String) {
        _uiState.update { it.copy(errorMessage = message, errorState = true, isLoading = false) }
    }

    /**
     * Fetches the list of applications from the repository.
     *
     * - Shows a loading indicator while fetching.
     * - Updates the UI state on success or failure.
     * - Logs relevant events for debugging.
     */
    fun getAllApplications() {
        Log.d(TAG, "Fetching all applications")

        // Show loading UI state (optional external function call)
        showLoading()

        // Launch coroutine in ViewModel scope (lifecycle-aware)
        viewModelScope.launch {
            // Update UI state to show loading in Compose
            _uiState.update { it.copy(isLoading = true) }

            // Call repository to fetch apps (from network or local DB fallback)
            val result = appRepository.fetchApps()

            // Handle the Result (fold: success or failure)
            result.fold(
                onSuccess = { appItems ->
                    // On successful fetch, update the UI state with app list and hide loading
                    _uiState.update { currentState ->
                        currentState.copy(
                            appsList = appItems,
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    // On failure, map the error type to a readable message
                    val errorMsg = when (error) {
                        is ApiError.BadRequest -> "Bad Request: ${error.message}"
                        is ApiError.Unauthorized -> "Unauthorized: ${error.message}"
                        is ApiError.Conflict -> "Conflict: ${error.message}"
                        is ApiError.ServerError -> "Server Error: ${error.message}"
                        is ApiError.UnknownError -> "Unknown Error: ${error.message}"
                        else -> "Unexpected error: ${error.localizedMessage}"
                    }

                    // Log the error message for debugging
                    Log.e(TAG, errorMsg)

                    // Show error to the user (optional external function call)
                    showError(errorMsg)
                }
            )
        }
    }
}