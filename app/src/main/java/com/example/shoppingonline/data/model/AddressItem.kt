package com.example.shoppingonline.Model

data class AddressItem(
    val addressId: String = "",
    val fullName: String = "",
    val phone: String = "",
    val fullAdress: String="",
    val province: String = "",
    val district: String = "",
    val ward: String = "",
    val note: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)
