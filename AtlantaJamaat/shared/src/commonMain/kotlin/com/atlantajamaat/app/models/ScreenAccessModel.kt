package com.atlantajamaat.app.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScreenAccessModel(
    @SerialName("ScreenCode") val screenCode: String,
    @SerialName("RoleCode") val roleCode: String,
    @SerialName("ScreenTypeCode") val screenTypeCode: String,
)