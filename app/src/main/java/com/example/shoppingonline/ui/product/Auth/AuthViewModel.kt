package com.example.shoppingonline.ui.product.Auth

import android.R
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingonline.Destination
import com.example.shoppingonline.Model.User
import com.example.shoppingonline.respository.AuthRepository
import com.example.shoppingonline.respository.SessionRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.core.Context
import kotlinx.coroutines.launch

class AuthViewModel(
     application: Application
) : AndroidViewModel(application) {
    private val authRepo = AuthRepository()
    private val sessionRepo = SessionRepository(application)
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    private val _destination = MutableLiveData<Destination>()
    val destination: LiveData<Destination> = _destination
     val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun register(
        email: String,
        password: String,
        name: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            authRepo.register(email, password, name)
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
        onError: (String) -> Unit,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            authRepo.login(email, password)
                .onSuccess {user ->
                    sessionRepo.saveUser(user)
                    onSuccess()
                }
                .onFailure {
                    onError("Đăng nhập thất bại")
                }
        }
    }
    fun start() {
        viewModelScope.launch {
            val loggedIn = sessionRepo.isLoggedIn()
            _destination.value =
                if (loggedIn) Destination.MAIN else Destination.LOGIN
        }
    }
    fun loadUser() {
        viewModelScope.launch {
            _user.value = authRepo.getUserFromSession(getApplication())
        }
    }
     fun logOut(onDone: () -> Unit){
        viewModelScope.launch {
            FirebaseAuth.getInstance().signOut()
            sessionRepo.clearSession()
            onDone()
        }

    }
    fun updateField(userId: String,filed:String,value: String){
        viewModelScope.launch {
            authRepo.updateAdetail(getApplication(),userId,filed,value)
                .onSuccess { _message.value=it}
                .onFailure { _message.value=it.message }
        }
    }
}
