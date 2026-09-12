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
    @SerialName("Token")
    val token: String? = null,

    @SerialName("MehmanId")
    val mehmanId: String? = null
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
    object NavigateToGuestLogin : LoginState()
    object NavigateToMemberLogin : LoginState()
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