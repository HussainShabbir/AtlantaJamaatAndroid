package com.atlantajamaat.app.network

import com.atlantajamaat.app.models.*
import com.atlantajamaat.app.models.LoginRequest
import com.atlantajamaat.app.models.LoginResponse
import com.atlantajamaat.app.models.LoginState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class LoginRepository {
    companion object {
        private val jsonParser = Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
        }

        private val httpClient = HttpClient {
            install(ContentNegotiation) {
                json(jsonParser)
            }
        }
    }

    suspend fun performLogin(itsId: String, userAgent: String, password: String = "", isGuestLogin: Boolean = false): LoginState = withContext(Dispatchers.IO) {
        return@withContext try {
            val request = LoginRequest(itsId, JAMAAT_ID, if (isGuestLogin) AUTH_TYPE_101 else AUTH_TYPE_102, MEDIUM, userAgent, VERSION, password)

            val httpResponse = httpClient.post(Endpoints.BASE_URL + Endpoints.LOGIN) {
                contentType(ContentType.Application.Json)
                headers.append(HttpHeaders.Accept, "application/json")
                setBody(request)
            }

            val responseText = httpResponse.bodyAsText()
            val response = jsonParser.decodeFromString<LoginResponse>(responseText)

            val token = response.token.orEmpty()
            val mehmanId = response.mehmanId
            val emptyToken = "00000000-0000-0000-0000-000000000000"

            when {
                token == emptyToken -> LoginState.Error("Please check with Jamaat Coordinator")
                !mehmanId.isNullOrBlank() -> LoginState.NavigateToGuestLogin
                else -> LoginState.NavigateToMemberLogin
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LoginState.Error(e.message ?: "Network call failed")
        }
    }

    // Only for Guest Users
    suspend fun forgotPassword(itsId: String, emailAddress: String): LoginState =  withContext(Dispatchers.IO) {
        return@withContext try {
            val httpResponse = httpClient.post(Endpoints.BASE_URL + Endpoints.FORGOT_PASSWORD) {
                contentType(ContentType.Application.Json)
                headers.append(HttpHeaders.Accept, "application/json")
                setBody(
                    mapOf(
                        "MemberITS" to "",
                        "MehmanITS" to itsId,
                        "EmailAddress" to emailAddress
                    )
                )
            }
            val responseText = httpResponse.bodyAsText()
            val response = jsonParser.decodeFromString<LoginResponse>(responseText)
            print("response ${response}")
            if (response.mehmanId == null) {
                LoginState.Error("Bad Its Id")
            } else {
                LoginState.Success
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LoginState.Error(e.message ?: "Network call failed")
        }
    }
}