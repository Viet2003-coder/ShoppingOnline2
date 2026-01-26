package com.example.shoppingonline.respository

import android.content.Context
import com.example.shoppingonline.CheckOnline
import com.example.shoppingonline.Model.CartItem
import com.example.shoppingonline.remote.api.RoomDatabase.AppDatabase
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Collections

class CartRepository(
    private val db: AppDatabase,
    ) {
    private val dbFireBase = FirebaseDatabase.getInstance().reference
    // TODO: Allow adding cart items while offline (Room as source of truth)
    suspend fun addToCart(context: Context, item: CartItem): Result<String> {
        val sucessString="Thêm giỏ hàng thành công"
        if (!CheckOnline.isOnline(context)){
            return Result.failure(Exception("Vui lòng kết nối internet"))
        }
        return runCatching {
            db.cartDao().insert(item)
            dbFireBase.child("carts").child(item.userId).child(item.productId).setValue(item)
            sucessString
        }
    }

    suspend fun getCart(userId: String): List<CartItem> {
        return db.cartDao().getAllCart(userId)
    }
    suspend fun deleteCart(context: Context,userId: String,productId: String): Result<String> {
        if (!CheckOnline.isOnline(context)){
            return Result.failure(Exception("Vui lòng kết nối internet"))
        }
        return runCatching {
            db.cartDao().deleteItem(userId,productId)
            dbFireBase.child("carts").child(userId).child(productId).removeValue()
            "Đã xóa khỏi giỏ hàng"
        }
    }
}