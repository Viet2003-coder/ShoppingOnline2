package com.example.shoppingonline.Model

import com.google.firebase.database.PropertyName

data class AddressItem(
    val uid: String="",
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
    @get:PropertyName("isDefault")
    @set:PropertyName("isDefault")
    var isDefault: Boolean = false
)
