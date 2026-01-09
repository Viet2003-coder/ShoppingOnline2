package com.example.shoppingonline.ui.product.adress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingonline.Model.AddressItem
import com.example.shoppingonline.respository.AddressRepository
import kotlinx.coroutines.launch

class AddressModel(
    val repo: AddressRepository= AddressRepository()
): ViewModel() {

    private val _addressItems = MutableLiveData<List<AddressItem>>()
    val addressItems: LiveData<List<AddressItem>> = _addressItems
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    fun loadAddress(userId: String){
        viewModelScope.launch {
            repo.getAddress(userId).onSuccess {list->
                _addressItems.value=list
            }.onFailure { exception ->
                _message.value=exception.message
            }
        }
    }
    fun addAdress(userId: String,address: AddressItem){
        viewModelScope.launch {
            repo.addAddress(userId,address).onSuccess {
                _message.value="Thêm địa chỉ thành công"
            }.onFailure { _message.value="Thêm địa chỉ lỗi" }
        }
    }
}