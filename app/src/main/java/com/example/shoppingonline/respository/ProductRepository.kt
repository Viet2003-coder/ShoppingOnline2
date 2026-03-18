package com.example.shoppingonline.respository

import android.util.Log
import com.example.shoppingonline.Model.Product
import com.example.shoppingonline.remote.api.RetrofitClient
import com.example.shoppingonline.data.model.toProduct
import com.example.shoppingonline.remote.api.RoomDatabase.AppDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class ProductRepository(
    private val db: AppDatabase,
) {
    private val dao = db.productDao()
    val dbFireBase = FirebaseDatabase.getInstance().getReference("products")
    val dbFireBaseCategory = FirebaseDatabase.getInstance().getReference("categories")
    suspend fun getProducts(
        limit: Int,
        page: Int
    ): List<Product> {
        val localCount = dao.count()
        val offset = limit * page
        if (localCount <= offset) {
            val respones = RetrofitClient.api.getBooks(limit, offset)
            val entities = respones.products.map { it.toProduct() }
            dao.insertAll(entities)
            val updates = entities.associate { it.id.toString() to it }
            dbFireBase.updateChildren(updates).await()
        }
        return dao.getPaged(limit, offset)
    }

    suspend fun getCategories(): List<String> {
        return try {
            val listCategory = mutableListOf<String>()
            val snapshot = dbFireBaseCategory.get().await()
            for (childSnapshot in snapshot.children) {
                val name = childSnapshot.child("name").getValue(String::class.java)
                name?.let { listCategory.add(it) }
            }
            listCategory
        } catch (e: Exception) {
            Log.e("Category", "Lỗi lấy categories: ${e.message}")
            emptyList()
        }
    }

    suspend fun getProduct(productId: String): Result<Product> {
        return try {
            val product = dao.getById(productId)
            if (product == null) {
                Result.failure(Exception("Not found product"))
            } else {
                Result.success(product)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProductStock(productId: String, newStock: Int): Result<Unit> {
        return try {
            dao.updateStock(productId, newStock)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun syncStockToFirebase(productId: String, newStock: Int) {
        try {
            val stockUpdate = mapOf("stock" to newStock)
            dbFireBase.child(productId).updateChildren(stockUpdate).await()
        } catch (e: Exception) {
            Log.e("Stockkkkk", e.toString())
        }
    }
}

