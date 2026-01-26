package com.example.shoppingonline.remote.api.RoomDatabase

import androidx.room.TypeConverter
import com.example.shoppingonline.Model.AddressItem
import com.example.shoppingonline.Model.OderItem
import com.example.shoppingonline.data.model.OderStatus
import com.google.gson.Gson
import okhttp3.Address

class OderCoverters {
    private val gSon= Gson()

    @TypeConverter
    fun orderItemToJson(item: OderItem): String =
        gSon.toJson(item)

    @TypeConverter
    fun jsonToOrderItem(json: String): OderItem =
        gSon.fromJson(json, OderItem::class.java)

    @TypeConverter
    fun addressToJson(address: AddressItem): String =
        gSon.toJson(address)

    @TypeConverter
    fun jsonToAddress(json: String): AddressItem =
        gSon.fromJson(json, AddressItem::class.java)

    @TypeConverter
    fun statusToString(status: OderStatus): String =
        status.name

    @TypeConverter
    fun stringToStatus(value: String): OderStatus =
        OderStatus.valueOf(value)
}