package com.example.shoppingonline.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName ="users",
)
data class User(
    @PrimaryKey
    val uid : String="",
    val name: String="",
    val email: String="",
    val phone: String = "",
    val avatarUrl: String = ""
)