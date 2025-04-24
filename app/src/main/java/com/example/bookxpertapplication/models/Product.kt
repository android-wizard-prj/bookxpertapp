package com.example.bookxpertapplication.models


data class Product(
    val id: String,
    val name: String,
    val data: Map<String, Any>?
)