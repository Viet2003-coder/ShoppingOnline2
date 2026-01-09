package com.example.shoppingonline.ui.product.adress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoppingonline.respository.AddressRepository

class AddressModelFactory(
    private val repo: AddressRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddressModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddressModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}