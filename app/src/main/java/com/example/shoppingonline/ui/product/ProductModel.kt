package com.example.shoppingonline.ui.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingonline.Model.Product
import com.example.shoppingonline.remote.api.RoomDatabase.AppDatabase
import com.example.shoppingonline.respository.ProductRepository
import kotlinx.coroutines.launch

class ProductModel(application: Application): AndroidViewModel(application) {
    val db= AppDatabase.get(application)
    private val repo = ProductRepository(db)
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products
    private val currentList = mutableListOf<Product>()
    private val _productSimple = MutableLiveData<Product>()
    val product: LiveData<Product> =_productSimple
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> =_message
    private var currentPage = 0
    private val limit = 30
    var isLoading = false
    fun loadProducts(){
        if (isLoading) return
        isLoading=true
        viewModelScope.launch {
            val pageItems=repo.getProducts(limit,currentPage)
            if (pageItems.isNotEmpty()){
                val current=_products.value?.toMutableList()?:mutableListOf()
                current.addAll(pageItems)
                _products.value=current
                currentPage++
            }
            isLoading=false
        }
    }
    fun getProduct(productId: String){
        viewModelScope.launch {
            val result=repo.getProduct(productId)
           result.onSuccess {product ->
               _productSimple.value=product
           }.onFailure {e->
               _message.value=e.message
           }
        }
    }
    fun updateStock(productId: String,newStock: Int){
        viewModelScope.launch {
            val result=repo.updateProductStock(productId,newStock)
            result.onSuccess { _message.value="Update thành công" }
                .onFailure { exception ->
                    _message.value=exception.message
                }
        }
    }
    fun syncStockToFireBase(productId: String,newStock: Int){
        viewModelScope.launch {
            val result=repo.syncStockToFirebase(productId,newStock)
        }
    }
}