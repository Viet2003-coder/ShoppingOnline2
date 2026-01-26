package com.example.shoppingonline.ui.product

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.shoppingonline.Model.CartItem
import com.example.shoppingonline.Model.User
import com.example.shoppingonline.R
import com.example.shoppingonline.Translate
import com.example.shoppingonline.UserSession
import com.example.shoppingonline.databinding.ActivityProductdetailBinding
import com.example.shoppingonline.ui.product.Auth.AuthViewModel
import com.example.shoppingonline.ui.product.Auth.Login
import com.example.shoppingonline.ui.product.cart.CartModel
import com.example.shoppingonline.ui.product.oder.BuyActivity

class Productdetail : AppCompatActivity() {
    private lateinit var binding: ActivityProductdetailBinding
    private val viewModel: CartModel by viewModels()
    private val AuthModel: AuthViewModel by viewModels()
    private var currenUser: User?=null

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProductdetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val title =intent.getStringExtra("product_title")?:""
        val id =intent.getStringExtra("product_id")?:""
        val price=intent.getStringExtra("product_price")?.toDoubleOrNull() ?:0.0
        val des= intent.getStringExtra("product_des")?:""
        val imgLink=intent.getStringExtra("product_img")?:""
        AuthModel.loadUser()
        AuthModel.user.observe(this){user ->
            currenUser=user
        }
        if (id.isEmpty()){
            Toast.makeText(this, "Product ID is null", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        binding.tvTitle.text=title
        binding.tvPrice.text=price.toString()
        binding.tvDesdetail.text=des
        Translate.translateEnToVi(des){
            runOnUiThread {
                binding.tvDesdetail.text=it
            }
        }

        Glide.with(this@Productdetail).load(imgLink)
            .placeholder(R.drawable.outline_download_for_offline_24)
            .error(R.drawable.outline_hide_image_24)
            .into(binding.imgProduct)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.btnAddToCart.setOnClickListener {
                val user=currenUser
                if (user==null){
                    startActivity(Intent(this, Login::class.java))
                    return@setOnClickListener
                }
                val userId= user.uid
                val cart= CartItem(userId , id, title, price, imgLink,des)
                viewModel.addToCart(this,cart)
        }
        viewModel.messages.observe(this){message->
            Toast.makeText(this,message.toString(), Toast.LENGTH_SHORT).show()
        }
        binding.btnBuyNow.setOnClickListener {
            val intent1= Intent(this@Productdetail, BuyActivity::class.java)
            intent1.putExtra("product_id",id)
            startActivity(intent1)
        }
    }

}