package com.example.shoppingonline.Model

data class Oder(
    val orderId: String = "",
    val userId: String = "",
    val items: OderItem = OderItem(),
    val shippingAddress: AddressItem = AddressItem(),
    val totalPrice: Double = 0.0,
    val status: String = "PENDING", // PENDING, CONFIRMED, SHIPPING, DONE, CANCEL
    val createdAt: Long = System.currentTimeMillis()
)