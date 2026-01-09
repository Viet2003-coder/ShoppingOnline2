package com.example.shoppingonline.ui.product.oder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.shoppingonline.UserSession
import com.example.shoppingonline.databinding.ActivityBuyBinding
import com.example.shoppingonline.ui.product.adress.ChooseAddress

class BuyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBuyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val product_id=intent.getStringExtra("product_id")
        val product_price=intent.getStringExtra("product_price")
        val product_title=intent.getStringExtra("product_title")
        val product_des=intent.getStringExtra("product_des")
        val product_image=intent.getStringExtra("product_image")
        val user= UserSession
        val name=user.currentUser?.name
        val numberPhone=user.currentUser?.phone
        val user_id=user.currentUser?.uid
        binding.tvProductName.text=product_title.toString()
        binding.tvProductPrice.text=product_price.toString()
//        binding.tvUserName.text=name.toString()
//        binding.tvUserPhone.text=numberPhone.toString()
        Glide.with(this@BuyActivity).load(product_image).into(binding.imgProduct)
        binding.btnChangeAddress.setOnClickListener {
            startActivity(Intent(this@BuyActivity, ChooseAddress::class.java))
        }
    }

}