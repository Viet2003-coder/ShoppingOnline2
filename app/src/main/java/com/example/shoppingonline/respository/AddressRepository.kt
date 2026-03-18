package com.example.shoppingonline.respository

import com.example.shoppingonline.Model.AddressItem
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class AddressRepository {
    val db= FirebaseDatabase.getInstance().getReference("addresses")
    suspend fun addAddress(address: AddressItem): Result<Unit> =try{
        db.child(address.uid)
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
    suspend fun setAddressDefault(user_id: String, addressId: String): Result<Unit>{
        return runCatching {
            val snapshot=db.child(user_id).get().await()
            val updates=mutableMapOf<String,Any?>()
            for (child in snapshot.children){
                val id=child.key?:continue
                updates["$id/isDefault"] = id==addressId
            }
            db.child(user_id).updateChildren(updates).await()
        }
    }
    suspend fun getAddressDefault(user_id: String): Result<AddressItem?> {
        return runCatching {
            val snapshot=db.child(user_id).orderByChild("isDefault").equalTo(true).get().await()
            if (snapshot.exists()){
                snapshot.children.firstOrNull()?.getValue(AddressItem::class.java)
            } else{
                null
            }
        }
    }
}