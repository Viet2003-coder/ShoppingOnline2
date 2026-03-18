package com.example.shoppingonline.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName ="users",
)
data class User(
    @PrimaryKey
    val uid : String="",
    var name: String="",
    val email: String="",
    var phone: String = "",
    var avatarUrl: String = ""
)