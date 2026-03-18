package com.example.shoppingonline.Application

import android.app.Application
import com.cloudinary.android.MediaManager

class UploadIMage: Application() {
    override fun onCreate() {
        super.onCreate()
        // 1. Cấu hình "Cái kho" (Chỉ cần làm 1 lần duy nhất khi app chạy)
        val config = mapOf(
            "cloud_name" to "draagtret",
            "api_key" to "477315579469953",
            "api_secret" to "BBNv4pNXhNUPwfXm4GbB4xr4eeo"
        )
        try {
            MediaManager.init(this, config)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}