package com.ot.doggies.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ImagesResponse(
    val message: List<String>,
    val status: String
)