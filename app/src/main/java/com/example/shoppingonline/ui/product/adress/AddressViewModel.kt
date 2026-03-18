package com.example.shoppingonline.ui.product.adress

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingonline.Model.AddressItem
import com.example.shoppingonline.respository.AddressRepository
import kotlinx.coroutines.launch

class AddressViewModel(
    val repo: AddressRepository = AddressRepository()
) : ViewModel() {

    private val _addressItems = MutableLiveData<List<AddressItem>>()
    val addressItems: LiveData<List<AddressItem>> = _addressItems
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    private val _addressDefault = MutableLiveData<AddressItem>()
    val addressDefault: LiveData<AddressItem?> = _addressDefault
    fun loadAddress(userId: String) {
        viewModelScope.launch {
            repo.getAddress(userId).onSuccess { list ->
                _addressItems.value = list
            }.onFailure { exception ->
                _message.value = exception.message
            }
        }
    }

    fun addAdress( address: AddressItem) {
        viewModelScope.launch {
            repo.addAddress(address).onSuccess {
                _message.value = "Thêm địa chỉ thành công"
            }.onFailure { _message.value = "Thêm địa chỉ lỗi" }
        }
    }

    fun setAddressDefault(userId: String, addressId: String) {
        viewModelScope.launch {
            repo.setAddressDefault(userId, addressId).onSuccess {
                _message.value = "Đặt địa chỉ mặc định thành công"
            }.onFailure { _message.value = "Đặt địa chỉ mặc định lỗi" }
        }
    }

    fun getAddressDefault(userId: String) {
        viewModelScope.launch {
            repo.getAddressDefault(userId).onSuccess {
                _addressDefault.value = it
            }.onFailure {
                _message.value = it.message
            }
        }
    }
}