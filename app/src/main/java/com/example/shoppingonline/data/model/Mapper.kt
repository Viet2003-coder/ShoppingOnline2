package com.example.shoppingonline.data.model

import com.example.shoppingonline.Model.Product

fun ProductApi.toProduct(): Product{
    return Product(
        id = this.id.toString(),   // ÉP id thành String
        title = this.title,
        description = this.description,
        price = this.price,
        category = this.category,
        thumbnail = this.thumbnail,
        stock = this.stock
    )
}