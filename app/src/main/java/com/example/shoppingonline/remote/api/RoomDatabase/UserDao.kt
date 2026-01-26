package com.example.shoppingonline.remote.api.RoomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.shoppingonline.Model.User
interface UserDao {
    @Dao
    interface UserDao {
        @Insert(onConflict = REPLACE)
        suspend fun insert(user: User)
        @Query("UPDATE users SET phone = :phone WHERE uid = :uid")
        suspend fun updatePhone(uid: String, phone: String)
    }

}