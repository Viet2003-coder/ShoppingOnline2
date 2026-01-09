package com.example.shoppingonline.ui.product.Auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingonline.Model.User
import com.example.shoppingonline.respository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun register(
        email: String,
        password: String,
        name: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            repo.register(email, password, name)
                .onSuccess {
                    onSuccess()
                }
                .onFailure { e ->
                    onError(e.message ?: "Đăng ký thất bại")
                }
        }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            repo.login(email, password)
                .onSuccess {
                    repo.getCurrentUser()
                        .onSuccess { onSuccess(it) }
                }
                .onFailure {
                    onError("Đăng nhập thất bại")
                }
        }
    }


}