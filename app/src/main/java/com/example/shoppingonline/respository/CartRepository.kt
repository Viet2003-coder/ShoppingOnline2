package com.example.shoppingonline.respository

import com.example.shoppingonline.Model.CartItem
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Collections

class CartRepository {
    private val db= FirebaseDatabase.getInstance().reference
    fun addToCart(userId: String,item: CartItem): Result<Unit>{
        return try {
            val result=db.child("carts").child(userId).child(item.productId).setValue(item)
            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun getCart(userId: String): List<CartItem> =
        suspendCancellableCoroutine { cont ->
            db.child("carts")
                .child(userId)
                .get()
                .addOnSuccessListener { snapshot ->
                    val list = mutableListOf<CartItem>()
                    snapshot.children.forEach {
                        it.getValue(CartItem::class.java)?.let { item ->
                            list.add(item)
                        }
                    }
                    cont.resume(list, null)
                }
                .addOnFailureListener {
                    cont.resume(Collections.emptyList(), null)
                }
        }
    }