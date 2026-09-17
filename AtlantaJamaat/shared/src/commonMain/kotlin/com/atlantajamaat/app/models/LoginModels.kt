package com.atlantajamaat.app.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

val JAMAAT_ID = "EF9EE9AC-5B08-4AAB-8F29-35115119C988"
val AUTH_TYPE_101 = 101
val AUTH_TYPE_102 = 102
val MEDIUM = "App"
val VERSION = "2"

@Serializable
data class LoginResponse(
    @SerialName("ItsId") val itsId: String,
    @SerialName("FirstName") val firstName: String,
    @SerialName("LastName") val lastName: String,
    @SerialName("Email") val email: String,
    @SerialName("MehmanId") val mehmanId: String,
    @SerialName("HOFFullName") val hofFullName: String,
    @SerialName("HofItsId") val hofItsId: String,
    @SerialName("HOFEmail") val hofEmail: String,
    @SerialName("Token") val token: String,
)

@Serializable
data class LoginWrapper(
    @SerialName("data")
    val data: LoginResponse? = null
)

// Result state for Business Logic
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Completed: LoginState()
    data class NavigateToGuestLogin(val response: LoginResponse) : LoginState()
    data class NavigateToMemberLogin(val response: LoginResponse) : LoginState()
    data class Success(val response: LoginResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}

@Serializable
data class LoginRequest(
    @SerialName("ITS") val its: String,
    @SerialName("JamaatID") val jamaatID: String,
    @SerialName("AuthType") val authType: Int = 102,
    @SerialName("Medium") val medium: String = "App",
    @SerialName("UserAgent") val userAgent: String = "Android",
    @SerialName("Version") val version: String = "2.0",
    @SerialName("Password") val password: String?,
)
