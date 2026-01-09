package com.example.shoppingonline.respository

import com.example.shoppingonline.Model.Product
import com.example.shoppingonline.RetrofitClient
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductRepository {
    val db= FirebaseDatabase.getInstance().getReference("products")
    suspend fun getProducts(  limit: Int,
                           skip: Int): List<Product>{
        val respone= RetrofitClient.api.getBooks(limit,skip).products ?: emptyList()
        respone.forEach { product ->
            saveProducttoFirebase(product)
        }
        return respone
    }
    suspend fun getTenProduct(): List<Product>{
        val list=mutableListOf<Product>()
        db.limitToFirst(10).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    child.getValue(Product::class.java)?.let {
                        list.add(it)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return list
    }

    private fun saveProducttoFirebase(product: Product) {
        db.child(product.id.toString()).get().addOnSuccessListener{
            if (!it.exists()){
                db.child(product.id.toString()).setValue(product)
            }
        }
    }
}