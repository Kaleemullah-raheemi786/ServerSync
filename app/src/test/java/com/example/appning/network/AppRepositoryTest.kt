package com.example.appning.network

import com.example.appning.database.dao.AppDao
import com.example.appning.database.model.AppEntity
import com.example.appning.dummy.dummyCachedApps
import com.example.appning.network.utils.ApiError
import com.example.appning.repository.AppRepository
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class AppRepositoryTest {

    private lateinit var mockHttpClient: HttpClient
    private lateinit var appRepository: AppRepository
    private lateinit var mockAppDao: AppDao
    private lateinit var json: Json

    private val BASE_URL = "https://ws2.aptoide.com/api/6"
    private val LIST_APPS_ENDPOINT = "bulkRequest/api_list/listApps"

    /**
     * Initializes test dependencies before each test run.
     * Sets up JSON configuration and mocks AppDao.
     */
    @Before
    fun setup() {
        json = Json {
            isLenient = true
            ignoreUnknownKeys = true
            explicitNulls = false
        }

        mockAppDao = mockk(relaxed = true)
    }

    /**
     * Test to verify that a successful API response results in a success result.
     * Mocks both API response and local database interactions.
     */
    @Test
    fun `fetchApps - always returns success`() = runTest {
        setupMockClientForSuccess()

        coEvery { mockAppDao.clearApps() } just Runs
        coEvery { mockAppDao.insertApps(any()) } just Runs

        coEvery { mockAppDao.getAllAppsImmediate() } returns listOf(
            AppEntity(
                id = 1L,
                name = "App 1",
                packageName = "com.example.app1",
                store_id = 1,
                store_name = "MainStore",
                vername = "1.0",
                vercode = 1,
                md5sum = "abc123",
                apk_tags = emptyList(),
                size = 1024L,
                downloads = 100,
                pdownloads = 500,
                added = "1620000000L",
                modified = "1620000001L",
                updated = "1620000002L",
                rating = 4.5,
                icon = "https://example.com/icon1.png",
                graphic = null,
                uptype = "normal"
            )
        )

        appRepository = AppRepository(mockHttpClient, mockAppDao)

        val result = appRepository.fetchApps()

        println("Test result: $result")

        assertTrue(result.isSuccess)

        val apps = result.getOrNull()
        assertNotNull(apps)
    }

    /**
     * Test to verify that a server-side error response results in a failure result.
     * Mocks an API error response and asserts the error type.
     */
    @Test
    fun `fetchApps - server error returns failure`() = runTest {
        setupMockClientForError(HttpStatusCode.InternalServerError)

        appRepository = AppRepository(mockHttpClient, mockAppDao)

        val result = appRepository.fetchApps()

        assertTrue(result.isFailure)
        val error = result.exceptionOrNull()
        assertNotNull(error)
        assertTrue(error is ApiError.UnknownError)
    }

    /**
     * Test to verify that when an exception occurs (e.g., network error),
     * the repository falls back to fetching cached apps from the local database.
     */
    @Test
    fun `fetchApps - exception triggers fallback to cached apps`() = runTest {
        // Mock a client that throws an exception on request
        mockHttpClient = HttpClient(MockEngine) {
            engine {
                addHandler { throw RuntimeException("Network error") }
            }
        }

        val dummyCachedApps = dummyCachedApps

        coEvery { mockAppDao.getAllAppsImmediate() } returns dummyCachedApps

        appRepository = AppRepository(mockHttpClient, mockAppDao)

        val result = appRepository.fetchApps()

        assertTrue(result.isSuccess)

        val apps = result.getOrNull()
        assertNotNull(apps)
        assertEquals(2, apps!!.size)
    }

    // --------------------------
    // MockEngine Helpers
    // --------------------------

    /**
     * Configures a mock HTTP client that simulates a successful API response.
     * Also verifies the request URL and returns a mock JSON payload.
     */
    private fun setupMockClientForSuccess() {
        mockHttpClient = HttpClient(MockEngine) {
            install(ContentNegotiation) { json(json) }
            engine {
                addHandler { request ->

                    assertEquals("$BASE_URL/$LIST_APPS_ENDPOINT", "${request.url}")
                    val mockResponse = """ 
                        // JSON response content here 
                    """.trimIndent()

                    respond(
                        content = ByteReadChannel(mockResponse),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            }
        }
    }

    /**
     * Configures a mock HTTP client that simulates an error response with the specified HTTP status code.
     */
    private fun setupMockClientForError(statusCode: HttpStatusCode) {
        mockHttpClient = HttpClient(MockEngine) {
            install(ContentNegotiation) { json(json) }
            engine {
                addHandler {
                    respond(
                        content = ByteReadChannel("Server Error"),
                        status = statusCode,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            }
        }
    }
}


