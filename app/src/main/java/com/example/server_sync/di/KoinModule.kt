package com.example.server_sync.di

import androidx.room.Room
import com.example.server_sync.database.db.AppDatabase
import com.example.server_sync.network.repository.AppRepository
import com.example.server_sync.viewmodel.MainViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Provide a singleton instance of HttpClient using OkHttp engine
    single {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                // Configure Kotlinx Serialization JSON settings
                json(Json {
                    ignoreUnknownKeys = true   // Ignore unknown fields in the JSON response
                    explicitNulls = false      // Don't serialize/deserialize explicit null values
                    isLenient = true           // Allow lenient parsing for relaxed JSON syntax
                })
            }
        }
    }

    // Provide a singleton instance of Room Database
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration(false) // Clears and recreates DB if schema changes
            .build()
    }

    // Provide a singleton instance of AppDao from the database
    single { get<AppDatabase>().appDao() }

    // Provide a singleton instance of AppRepository, injecting HttpClient and AppDao
    single { AppRepository(get(), get()) }

    // Provide a ViewModel instance for MainViewModel, injecting AppRepository
    viewModel { MainViewModel(get()) }
}
