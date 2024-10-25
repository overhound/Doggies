package com.ot.doggies.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BreedListResponse(
    val message: Map<String, List<String>>,
    val status: String
)