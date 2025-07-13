📱Project Overview

This is a Kotlin-based Android application built with Jetpack Compose, following a clean MVVM architecture pattern. 
The project uses Koin for dependency injection and Ktor as the HTTP client. The app fetches a list of applications 
from a remote API, displays them in both a list and detail screen, caches them locally, and notifies users when new 
apps become available.

✅ Features Developed Fetch applications list via remote API using Ktor HttpClient

API Request Error Handling

Cache fetched apps into Room Database

Support offline access using cached data

Periodic background notifications for new apps using WorkManager

Dependency Injection using Koin

Clean MVVM architecture

Unit tests using JUnit, Mockk, and Coroutine Test

Notification Channel for Android 8.0+ compatibility