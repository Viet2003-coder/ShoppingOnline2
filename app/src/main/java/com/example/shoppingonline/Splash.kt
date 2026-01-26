package com.example.shoppingonline

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppingonline.ui.product.Auth.AuthViewModel
import com.example.shoppingonline.ui.product.Auth.Login

class Splash : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalash)
        viewModel.destination.observe(this) {
            when (it) {
                Destination.MAIN ->
                    startActivity(Intent(this, MainActivity::class.java))
                Destination.LOGIN ->
                    startActivity(Intent(this, Login::class.java))
            }
            finish()
        }
        viewModel.start()
    }
    }