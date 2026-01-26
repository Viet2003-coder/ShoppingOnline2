package com.example.shoppingonline

import androidx.datastore.preferences.core.stringPreferencesKey

object UserSession {
    val userId= stringPreferencesKey("userId")
    val fullname= stringPreferencesKey("fullname")
    val phone= stringPreferencesKey("phone")
    val email= stringPreferencesKey("email")
}