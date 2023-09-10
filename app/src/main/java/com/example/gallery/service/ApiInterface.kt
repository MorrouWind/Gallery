package com.example.gallery.service

import com.example.gallery.model.ImageModel
import com.example.gallery.model.SearchModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("photos")
    fun getImagesResponse(
        @Query("page") page: Int,
        @Query("per_page") pageLimit: Int,
    ) : Call<MutableList<ImageModel>>

    @GET("search/photos")
    fun searchImages(
        @Query("query") query: String
    ) : Call<SearchModel>

}