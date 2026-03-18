package com.example.shoppingonline.ui.product.Comment

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingonline.data.model.Comment
import com.example.shoppingonline.respository.CommentRepository
import kotlinx.coroutines.launch

class CommentViewModel(
    private val repository: CommentRepository
): ViewModel() {
    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> = _comments
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    fun fetchComments(productId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            repository.getComments(productId) { comments ->
                _comments.value = comments
                _isLoading.value = false
            }
        }
    }
    fun addComment(context: Context, comment: Comment) {
        viewModelScope.launch {
            val result = repository.addToComment(context,comment)
            result.onSuccess { message ->
                _message.value = message}.onFailure { exception ->
                _message.value = exception.message
            }
        }
    }
    }