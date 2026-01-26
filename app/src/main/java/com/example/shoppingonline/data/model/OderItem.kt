package com.example.shoppingonline.Model

data class OderItem(
    val productId: String = "",
    val title: String = "",
    val price: Double = 0.0,
    val thumbnail: String = "",
    val description: String="",
    val stock: Int=0
)
