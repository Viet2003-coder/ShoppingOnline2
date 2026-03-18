package com.example.shoppingonline

import androidx.datastore.preferences.core.stringPreferencesKey

object UserSession {
    val userId= stringPreferencesKey("userId")
    val name= stringPreferencesKey("name")
    val phone= stringPreferencesKey("phone")
    val email= stringPreferencesKey("email")
    val avatarUrl= stringPreferencesKey("avatarUrl")
}