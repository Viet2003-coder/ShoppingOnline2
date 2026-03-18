package com.example.shoppingonline.ui.product.cart

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shoppingonline.Model.CartItem
import com.example.shoppingonline.remote.api.RoomDatabase.AppDatabase
import com.example.shoppingonline.respository.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application){
    val db= AppDatabase.get(application)
    private val repo = CartRepository(db)
    private val _cart = MutableLiveData<List<CartItem>>()
    val carts: LiveData<List<CartItem>> = _cart
    private val _message = MutableLiveData<String>()
    val messages: LiveData<String> = _message
    val _cartCount = MutableLiveData<Int>()
    val cartCount: LiveData<Int> = _cartCount

    fun addToCart(
        context: Context,
        cartItem: CartItem
    ) {
        viewModelScope.launch {
            repo.addToCart(context,cartItem)
                .onSuccess {  _message.value=it }
                .onFailure { _message.value=it.message }
        }

    }
    fun loadCart(
        userId: String
    ){
        viewModelScope.launch {
            _cart.value=repo.getCart(userId)
            _cartCount.value=repo.getCart(userId).size
        }
    }
    fun deleteCart(context: Context,userId: String,productId: String){
        viewModelScope.launch {
            repo.deleteCart(context,userId,productId)
                .onSuccess { _message.value=it }
                .onFailure { _message.value=it.message }
        }
    }
}