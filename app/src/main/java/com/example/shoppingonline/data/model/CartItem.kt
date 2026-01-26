package com.example.shoppingonline.Model

import androidx.room.Entity

@Entity(tableName = "carts",
        primaryKeys = ["userId", "productId"]

)
data class CartItem(
    val userId: String,
    val productId: String ,
    val title: String ,
    val price: Double ,
    val thumbnail: String,
    val description: String,
)
