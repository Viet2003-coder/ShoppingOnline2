package com.example.shoppingonline.ui.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shoppingonline.Model.Product
import com.example.shoppingonline.remote.api.RoomDatabase.AppDatabase
import com.example.shoppingonline.respository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(application: Application): AndroidViewModel(application) {
    val db= AppDatabase.get(application)
    private val repo = ProductRepository(db)
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products
    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> = _categories
    private var currentList = mutableListOf<Product>()
    private val _productSimple = MutableLiveData<Product>()
    val product: LiveData<Product> =_productSimple
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> =_message
    private var currentPage = 0
    private val limit = 30
    var isLoading = false
    private var currentCategory = "Tất cả"
    private var isEndOfData = false // Biến đánh dấu đã hết sạch dữ liệu
    private var currentQuerySearch=""
    fun loadProducts(){
        if (isLoading||isEndOfData) return
        isLoading=true
        viewModelScope.launch {
            try {
                val pageItems=repo.getProducts(limit,currentPage)
                if (pageItems.isEmpty()){
                    isEndOfData=true
                }
                else{
                    currentList.addAll(pageItems)
                    currentPage++
                    applyFilter()
                    isLoading=false
                    val filteredCount=_products.value?.size ?: 0
                    if (currentCategory!="Tất cả"&&filteredCount<6 && !isEndOfData){
                        loadProducts()
                    }
                }
            } catch (e : Exception){
                _message.value=e.message
            } finally {
                isLoading=false
            }
        }
    }
    // 4. Hàm thực hiện lọc (Helper function)
    private fun applyFilter() {
        var filtered=currentList.toList()
        if (currentCategory != "Tất cả") {
            filtered = filtered.filter { it.category == currentCategory }
        }
        if (currentQuerySearch.isNotEmpty()){
            filtered=filtered.filter { it.title.contains(currentQuerySearch,ignoreCase = true) }
        }
        _products.value=filtered
    }
    // 5. Hàm công khai cho Fragment gọi khi nhấn Chip
    fun filterByCategory(categoryName: String) {
        currentCategory = categoryName
        applyFilter()
        // Nếu lọc xong mà không có sản phẩm nào, tự động gọi load thêm trang mới
        val filteredCount = _products.value?.size ?: 0
        if (categoryName != "Tất cả" && filteredCount < 6) {
            loadProducts()
        }
    }
    fun setSearchQuery(query: String){
        currentQuerySearch=query
        applyFilter()
    }

    fun getCategories() {
        viewModelScope.launch {
            val result = repo.getCategories()
            _categories.value = result
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