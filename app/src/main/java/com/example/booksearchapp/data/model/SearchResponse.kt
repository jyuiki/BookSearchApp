package com.example.booksearchapp.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("documents")
    val documents: List<Book>,
    @SerialName("meta")
    val meta: Meta
)
