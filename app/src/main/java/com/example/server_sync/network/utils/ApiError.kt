package com.example.server_sync.network.utils

sealed class ApiError : Throwable() {
    data class BadRequest(val errorMessage: String?) : ApiError()
    data class Unauthorized(val errorMessage: String?) : ApiError()
    data class Conflict(val errorMessage: String?) : ApiError()
    data class ServerError(val errorMessage: String?) : ApiError()
    data class UnknownError(val errorMessage: String?) : ApiError()
}