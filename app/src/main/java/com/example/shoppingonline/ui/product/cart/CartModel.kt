package com.example.shoppingonline.ui.product.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingonline.Model.CartItem
import com.example.shoppingonline.respository.CartRepository
import kotlinx.coroutines.launch

class CartModel(private val repo : CartRepository = CartRepository()) : ViewModel(){
    private val _cart = MutableLiveData<List<CartItem>>()
    val carts: LiveData<List<CartItem>> = _cart
    fun addToCart(
        userId: String,
        cartItem: CartItem,
        onSucess:() -> Unit,
        onError:(String)-> Unit
    ) {
        viewModelScope.launch {
            repo.addToCart(userId,cartItem)
                .onSuccess { onSucess() }
                .onFailure { onError(it.message ?: "Thêm không thành công") }
        }

    }
    fun loadCart(
        userId: String
    ){
        viewModelScope.launch {
            _cart.value=repo.getCart(userId)
        }
    }
}