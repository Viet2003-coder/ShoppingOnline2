package com.example.shoppingonline.data.model

data class ProductApi(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val thumbnail: String,
    val category: String,
    val images: List<String>
)
