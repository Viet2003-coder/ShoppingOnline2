package com.example.shoppingonline.remote.api.RoomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.shoppingonline.Model.Oder
import com.example.shoppingonline.Model.Product
import com.example.shoppingonline.data.model.OderStatus

@Dao
interface OderDao {
    @Query("SELECT * FROM oders WHERE userId = :userId")
    suspend fun getById(userId: String): List<Oder>
    @Insert
    suspend fun insert(order: Oder)
    @Query("""
    UPDATE oders 
    SET status = :status 
    WHERE userId = :userId AND orderId = :orderId
""")
    suspend fun updateStatus(
        userId: String,
        orderId: String,
        status: OderStatus
    )

}