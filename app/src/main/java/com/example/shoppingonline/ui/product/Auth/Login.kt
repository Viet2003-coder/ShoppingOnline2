package com.example.shoppingonline.ui.product.Auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.shoppingonline.MainActivity
import com.example.shoppingonline.UserSession
import com.example.shoppingonline.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.login(
                email = binding.edtEmail.text.toString(),
                password = binding.edtPassword.text.toString(),
                onSuccess = {
                    Toast.makeText(this@Login,"Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(Intent(this@Login, MainActivity::class.java))
                    finish() },
                onError = {message->
                    Toast.makeText(this@Login,message, Toast.LENGTH_SHORT).show()
                }
            )
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this@Login, Register::class.java))
        }
    }
}