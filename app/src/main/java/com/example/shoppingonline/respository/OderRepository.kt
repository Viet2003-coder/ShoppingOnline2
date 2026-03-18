package com.example.shoppingonline.respository

import android.content.Context
import com.example.shoppingonline.CheckOnline
import com.example.shoppingonline.Model.Oder
import com.example.shoppingonline.data.model.OderStatus
import com.example.shoppingonline.remote.api.RoomDatabase.AppDatabase
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class OderRepository(
    private val db: AppDatabase
    ) {
    val dbr= FirebaseDatabase.getInstance().getReference("oders")
    suspend fun addOder(context: Context, oder: Oder): Result<String>{
        if (!CheckOnline.isOnline(context)){
            return Result.failure(Exception("Vui lòng kết nối internet"))
        }
        return runCatching {
            db.oderDao().insert(oder)
            dbr.child(oder.userId).child(oder.orderId).setValue(oder).await()
            "Done"
        }
    }
    suspend fun getOder(userId: String): Result<List<Oder>> =try {
        val list=db.oderDao().getById(userId)
        Result.success(list)
    } catch (e: Exception){
        Result.failure(e)
    }
     suspend fun updateStatus(userId: String, orderId: String, status: OderStatus){
        db.oderDao().updateStatus(userId,orderId,status)
    }
    suspend fun getOderDetail(context: Context,userId: String, orderId: String): Result<Oder>{
        if (!CheckOnline.isOnline(context)){
            return Result.failure(Exception("Vui lòng kết nối internet"))
        }
        return runCatching {
            val snapshot=dbr.child(userId).child(orderId).get().await()
            val oder=snapshot.getValue(Oder::class.java)
            oder?:throw Exception("Không tìm thấy đơn hàng")
        }
    }
}