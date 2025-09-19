package com.example.server_sync.state

import android.app.AlertDialog
import com.example.server_sync.network.model.AppItem

data class MainUiState(
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val alert: AlertDialog? = null,
    val successState: Boolean = false,
    val errorState: Boolean = false,
    var isLoading: Boolean = false,
    val appsList: List<AppItem> = emptyList()
)



