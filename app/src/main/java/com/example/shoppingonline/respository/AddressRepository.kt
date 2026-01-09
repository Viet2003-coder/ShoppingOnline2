package com.example.shoppingonline.respository

import com.example.shoppingonline.Model.AddressItem
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class AddressRepository {
    val db= FirebaseDatabase.getInstance().getReference("addresses")
    suspend fun addAddress(user_id:String,address: AddressItem): Result<Unit> =try{
        db.child(user_id)
            .child(address.addressId)
            .setValue(address).await()
        Result.success(Unit)
    } catch (e: Exception){
        Result.failure(e)
    }
    suspend fun getAddress( user_id: String): Result<List<AddressItem>> = try{
        val snapshot=db.child(user_id).get().await()
        val list=snapshot.children.mapNotNull { it.getValue(AddressItem::class.java) }
        Result.success(list)
    } catch (e: Exception){
        Result.failure(e)
    }
}