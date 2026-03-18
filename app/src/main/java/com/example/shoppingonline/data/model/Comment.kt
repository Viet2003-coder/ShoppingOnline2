package com.example.shoppingonline.data.model

data class Comment(
    val userId: String="",
    val userName: String="",
    val productId: String="",
    val content: String="",
    val rating: Float=0f,
    val createdAt: Long = System.currentTimeMillis(),
    val imageUrl: String=""
)
