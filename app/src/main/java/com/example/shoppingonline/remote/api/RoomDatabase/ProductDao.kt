package com.example.shoppingonline.remote.api.RoomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shoppingonline.Model.Product
@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    suspend fun getAll(): List<Product>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getById(id: String): Product?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<Product>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product)
    @Query("UPDATE products SET stock = :newStock WHERE id = :id")
    suspend fun updateStock(id: String, newStock: Int)
    @Query("DELETE FROM products")
    suspend fun clear()
    @Query("SELECT COUNT(*) FROM products")
    suspend fun count(): Int
    @Query("SELECT * FROM products ORDER BY id LIMIT :limit OFFSET :offset")
    suspend fun getPaged(limit: Int, offset: Int): List<Product>
}