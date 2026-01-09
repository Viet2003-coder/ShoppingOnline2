package com.example.shoppingonline.respository

import com.example.shoppingonline.remote.api.firebase.AthuFirebaseDataReSource
import com.example.shoppingonline.Model.User
import com.example.shoppingonline.remote.api.firebase.UserFirebaseDataResource
import com.google.firebase.auth.FirebaseAuth

class AuthRepository(
    private val authDS: AthuFirebaseDataReSource = AthuFirebaseDataReSource(),
    private val userDS: UserFirebaseDataResource = UserFirebaseDataResource()
) {

    suspend fun register(
        email: String,
        password: String,
        name: String
    ): Result<Unit> {
        return try {
            val result = authDS.register(email, password)
            val uid = result.user?.uid
                ?: return Result.failure(kotlin.Exception("Không lấy được UID"))

            val user = User(uid, name, email)
            userDS.saveUser(user)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            authDS.login(email, password)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getCurrentUser(): Result<User> {
        return try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
                ?: return Result.failure(Exception("Chưa đăng nhập"))

            val user = userDS.getUser(uid)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}