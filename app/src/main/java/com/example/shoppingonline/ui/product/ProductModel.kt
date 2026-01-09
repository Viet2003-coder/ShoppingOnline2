package com.example.shoppingonline.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingonline.Model.Product
import com.example.shoppingonline.respository.ProductRepository
import kotlinx.coroutines.launch

class ProductModel: ViewModel() {
    private val repo = ProductRepository()
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products
    private val _tenproducts = MutableLiveData<List<Product>>()
    val tenproducts: LiveData<List<Product>> = _products
    private val currentList = mutableListOf<Product>()

    private var currentPage = 0
    private val limit = 30
    private var isLoading = false
    fun loadTenProduct(){
        viewModelScope.launch {
            _tenproducts.value=repo.getTenProduct()
        }
    }
    fun loadProducts(){
        if (isLoading) return
        isLoading=true
        viewModelScope.launch {
            val currentList = _products.value?.toMutableList() ?: mutableListOf()

            val newItems = repo.getProducts(
                limit = limit,
                skip = currentPage * limit
            )

            // ⛔ tránh trùng
            val filtered = newItems.filter { new ->
                currentList.none { it.id == new.id }
            }

            currentList.addAll(filtered)
            _products.value = currentList

            currentPage++
            isLoading = false
        }
    }
}