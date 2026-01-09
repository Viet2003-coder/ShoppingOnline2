package com.example.shoppingonline.remote.api

import com.example.shoppingonline.data.model.ProductRespone
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApi {
    @GET("products")
    suspend fun getBooks(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ProductRespone
}