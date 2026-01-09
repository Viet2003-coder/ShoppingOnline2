package com.example.shoppingonline.ui.product.Auth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppingonline.databinding.ActivityRegisterBinding

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarRegister)
        // ✅ Hiện nút back
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // ✅ Bắt sự kiện click back
        binding.toolbarRegister.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.btnRegister.setOnClickListener {
            viewModel.register(
                email = binding.edtEmail.text.toString(),
                password = binding.edtPassword.text.toString(),
                name = binding.edtName.text.toString(),
                onSuccess = {
                    Toast.makeText(
                        this@Register,
                        "Đăng ký thành công",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onError = { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}