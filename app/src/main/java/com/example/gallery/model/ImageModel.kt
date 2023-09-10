package com.example.gallery.model

import com.google.gson.annotations.SerializedName

data class ImageModel(
    @SerializedName("id") val id: String,
    @SerializedName("urls") val urls: UrlModel,
    @SerializedName("user") val user: UserModel,
    @SerializedName("color") val color: String,
    @SerializedName("description") val description: String,
    @SerializedName("likes") val likes: String,
)