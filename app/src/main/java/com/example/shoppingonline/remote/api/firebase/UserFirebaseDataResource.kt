package com.example.shoppingonline.remote.api.firebase

import com.example.shoppingonline.Model.User
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class UserFirebaseDataResource {
     val ref = FirebaseDatabase
        .getInstance()
        .getReference("users")

    suspend fun saveUser(user: User) {
        ref.child(user.uid).setValue(user).await()
    }
    suspend fun getUser(uid: String): User {
        return ref.child(uid).get().await().getValue(User::class.java)
            ?: throw Exception("User not found")
    }
}