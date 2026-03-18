package com.example.shoppingonline.Model

import androidx.room.Entity
import com.example.shoppingonline.data.model.OderStatus

@Entity(tableName = "oders",
    primaryKeys = ["userId", "orderId"]
)
data class Oder(
    val userId: String="",
    val orderId: String = "",
    val item: OderItem = OderItem(),
    val shippingAddress: AddressItem = AddressItem(),
    val totalPrice: Double = 0.0,
    val status: OderStatus= OderStatus.PENDING, // PENDING, CONFIRMED, SHIPPING, DONE, CANCEL
    val shippingFee: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis(),
)