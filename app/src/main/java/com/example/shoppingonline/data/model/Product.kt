package com.example.shoppingonline.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var stock: Int = 0,
    var category: String = "",
    var thumbnail: String = "",
)
