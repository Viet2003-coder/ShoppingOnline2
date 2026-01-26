package com.example.shoppingonline.ui.product.oder

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingonline.Model.Oder
import com.example.shoppingonline.remote.api.RoomDatabase.AppDatabase
import com.example.shoppingonline.respository.OderRepository
import com.example.shoppingonline.respository.ProductRepository
import kotlinx.coroutines.launch

class OderModel(
    application: Application
): AndroidViewModel(application) {
    val db= AppDatabase.get(application)
    val repo= OderRepository(db)
    val productRepo= ProductRepository(db)
    val _oders= MutableLiveData<List<Oder>>()
    val oder: LiveData<List<Oder>> =_oders
    val _message= MutableLiveData<String>()
    val messages: LiveData<String> =_message
    val _orderCount = MutableLiveData<Int>()
    val orderCount: LiveData<Int> = _orderCount

    fun addOder(context: Context, oder: Oder){
        viewModelScope.launch {
            repo.addOder(context,oder)
                .onSuccess {msg->
                    _message.value=msg
                }.onFailure {
                    _message.value=it.message
                }
        }
    }
    fun getOders(userId: String){
        viewModelScope.launch {
            repo.getOder(userId).onSuccess { list->
                _oders.value=list
                _orderCount.value=list.size
            }.onFailure {
                _message.value=it.message
            }
        }
    }
}