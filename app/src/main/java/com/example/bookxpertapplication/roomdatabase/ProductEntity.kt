package com.example.bookxpertapplication.roomdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    var name: String,
    val dataJson: String? // Store data as JSON string
)