package com.example.shoppingonline.respository

import android.annotation.SuppressLint
import androidx.datastore.preferences.core.edit
import com.example.shoppingonline.Model.User
import com.example.shoppingonline.UserSession
import com.example.shoppingonline.dataStore
import kotlinx.coroutines.flow.first

class SessionRepository(
   private val context: android.content.Context
) {
    suspend fun saveUser(user: User){
        context.dataStore.edit { prefs ->
            prefs[UserSession.userId] = user.uid
            prefs[UserSession.fullname] = user.name
            prefs[UserSession.phone] = user.phone
            prefs[UserSession.email] = user.email
        }
    }
    suspend fun isLoggedIn(): Boolean {
        val prefs = context.dataStore.data.first()
        return prefs[UserSession.userId] != null
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}