package com.atlantajamaat.app.network

import com.atlantajamaat.app.models.ScreenAccessModel
import com.atlantajamaat.app.network.LoginRepository.Companion.httpClient
import com.atlantajamaat.app.network.LoginRepository.Companion.jsonParser
import com.russhwolf.settings.Settings
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class ScreenAccessRepository {
    private val settings = Settings()

    suspend fun getScreenAccessInfo(): List<ScreenAccessModel> {
        return try {
            val tokenValue = settings.getString("AUTH_TOKEN", "0000")
            val request = mapOf("Token" to mapOf("Token" to tokenValue))

            val httpResponse = httpClient.post(Endpoints.BASE_URL + Endpoints.SCREEN_ACCESS) {
                contentType(ContentType.Application.Json)
                headers.append(HttpHeaders.Accept, "application/json")
                setBody(request)
            }

            val responseText = httpResponse.bodyAsText()

            // Decodes directly into a List of JSON elements
            jsonParser.decodeFromString<List<ScreenAccessModel>>(responseText)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}
