package com.example.gallery.model

import com.google.gson.annotations.SerializedName

data class UrlModel(
    @SerializedName("full") val full: String,
    @SerializedName("regular") val regular: String,
)
