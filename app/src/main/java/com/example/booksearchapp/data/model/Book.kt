package com.example.booksearchapp.data.model


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity(tableName = "books")
data class Book(
    @SerialName("authors")
    val authors: List<String>,
    @SerialName("contents")
    val contents: String,
    @SerialName("datetime")
    val datetime: String,
    @PrimaryKey(autoGenerate = false)
    @SerialName("isbn")
    val isbn: String,
    @SerialName("price")
    val price: Int,
    @SerialName("publisher")
    val publisher: String,
    @ColumnInfo(name="sale_price")
    @SerialName("sale_price")
    val salePrice: Int,
    @SerialName("status")
    val status: String,
    @SerialName("thumbnail")
    val thumbnail: String,
    @SerialName("title")
    val title: String,
    @SerialName("translators")
    val translators: List<String>,
    @SerialName("url")
    val url: String
) : Parcelable
