package com.example.bookxpertapplication.interfaces

import com.example.bookxpertapplication.models.Product
import retrofit2.http.GET

interface ApiService {
    @GET("objects")
    suspend fun getProducts(): List<Product>
}
