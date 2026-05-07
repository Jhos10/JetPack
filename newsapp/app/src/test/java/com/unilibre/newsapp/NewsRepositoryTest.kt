package com.unilibre.newsapp

import com.unilibre.newsapp.data.remote.NewsApiService
import com.unilibre.newsapp.data.repository.NewsRepositoryImpl
import com.unilibre.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsRepositoryTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: NewsApiService
    private lateinit var repository: NewsRepository

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
        repository = NewsRepositoryImpl(api)
    }

    @Test
    fun `getTopHeadlines returns success when response is 200`() = runTest {
        // Arrange
        val mockJson = """
            { "status": "ok", "totalResults": 1,
              "articles": [{ "title": "Test News", "source": { "name": "CNN" } }] }
        """.trimIndent()
        mockWebServer.enqueue(MockResponse().setBody(mockJson).setResponseCode(200))

        // Act
        val result = repository.getTopHeadlines()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals("Test News", result.getOrNull()?.first()?.title)
    }

    @Test
    fun `getTopHeadlines returns failure when response is 500`() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(500))
        val result = repository.getTopHeadlines()
        assertTrue(result.isFailure)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
