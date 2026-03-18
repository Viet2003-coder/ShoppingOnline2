package com.example.shoppingonline.respository

import android.content.Context
import android.net.Uri
import androidx.datastore.preferences.core.edit
import com.example.shoppingonline.CheckOnline
import com.example.shoppingonline.remote.api.firebase.AthuFirebaseDataReSource
import com.example.shoppingonline.Model.User
import com.example.shoppingonline.UserSession
import com.example.shoppingonline.dataStore
import com.example.shoppingonline.remote.api.firebase.UserFirebaseDataResource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException


class AuthRepository(
    private val authDS: AthuFirebaseDataReSource = AthuFirebaseDataReSource(),
    private val userDS: UserFirebaseDataResource = UserFirebaseDataResource(),
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
    suspend fun login(email: String, password: String): Result<User> {
        return try {
            authDS.login(email, password)
            val uid = FirebaseAuth.getInstance().currentUser?.uid
                ?: return Result.failure(Exception("Chưa đăng nhập"))
            val user = userDS.getUser(uid)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getUserFromSession(context: Context): User? {
        val prefs = context.dataStore.data.first()
        val uid = prefs[UserSession.userId] ?: return null
        val name = prefs[UserSession.name] ?: ""
        val phone = prefs[UserSession.phone] ?: ""
        val email = prefs[UserSession.email] ?: ""
        val avatarUrl=prefs[UserSession.avatarUrl]?:""
        return User(
            uid = uid,
            name = name,
            phone = phone,
            email = email,
            avatarUrl = avatarUrl
        )
    }
    suspend fun updateAdetail(context: Context, userId: String,field: String,value: String): Result<String>{
        val successString="Cập nhật thành công"
        if (!CheckOnline.isOnline(context)){
            return Result.failure(Exception("Vui lòng kết nối internet"))
        }
        return runCatching {
            userDS.ref.child(userId).child(field).setValue(value).await()
            context.dataStore.edit { prefs ->
                if (field=="name"){
                    prefs[UserSession.name] = value
                }
                if (field=="phone"){
                    prefs[UserSession.phone] = value
                }
            }
            successString
        }
    }
    suspend fun uploadAvatar(imageUri: Uri): Result<String> =
        suspendCancellableCoroutine { continuation ->

            MediaManager.get().upload(imageUri)
                .option("folder", "avatars") // thư mục trên cloudinary
                .callback(object : UploadCallback {

                    override fun onStart(requestId: String?) {}

                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

                    override fun onSuccess(
                        requestId: String?,
                        resultData: Map<*, *>
                    ) {
                        val imageUrl = resultData["secure_url"].toString()
                        continuation.resume(Result.success(imageUrl), null)
                    }

                    override fun onError(
                        requestId: String?,
                        error: ErrorInfo?
                    ) {
                        continuation.resumeWithException(
                            Exception(error?.description ?: "Upload failed")
                        )
                    }

                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
                })
                .dispatch()
        }
    suspend fun updateAvatar(context: Context, userId: String, avatarUrl: String): Result<String>{
        val successString="Cập nhật ảnh đại diện thành công"
        if (!CheckOnline.isOnline(context)){
            return Result.failure(Exception("Vui lòng kết nối internet"))
        }
        // 2. Cập nhật avatarUrl
        val userUpdate = mapOf(
            "avatarUrl" to avatarUrl
        )
        return runCatching {
            userDS.ref.child(userId).updateChildren(userUpdate).await()
            context.dataStore.edit { prefs ->
                prefs[UserSession.avatarUrl] = avatarUrl
            }
            successString
        }
    }
    suspend fun resetPassword(context: Context,email: String): Result<String>{
        val successString="Đã gửi email đổi mật khẩu "
        if (!CheckOnline.isOnline(context)){
            return Result.failure(Exception("Vui lòng kết nối internet"))
        }
       return runCatching {
            val auth: FirebaseAuth = FirebaseAuth.getInstance()
            auth.sendPasswordResetEmail(email).await()
            successString
        }
    }
}