package com.example.shoppingonline.remote.api.RoomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shoppingonline.Model.CartItem

@Dao
interface CartDao {
    @Query("SELECT * FROM carts where userId = :userId")
    suspend fun getAllCart(userId: String): List<CartItem>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartItem)
    @Query("""
        DELETE FROM carts 
        WHERE userId = :userId AND productId = :productId
    """)
    suspend fun deleteItem(userId: String, productId: String)
}