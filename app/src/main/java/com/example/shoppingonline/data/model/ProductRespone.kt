package com.example.shoppingonline.data.model

import com.example.shoppingonline.Model.Product

data class ProductRespone(
    val products: List<ProductApi>,
    val total: Int,
    val skip: Int,
    val limit: Int
)