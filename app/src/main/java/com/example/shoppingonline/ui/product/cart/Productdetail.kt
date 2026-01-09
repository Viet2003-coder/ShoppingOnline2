package com.example.shoppingonline.ui.product.cart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.shoppingonline.ui.product.oder.BuyActivity
import com.example.shoppingonline.Model.CartItem
import com.example.shoppingonline.R
import com.example.shoppingonline.UserSession
import com.example.shoppingonline.databinding.ActivityProductdetailBinding

class Productdetail : AppCompatActivity() {
    private lateinit var binding: ActivityProductdetailBinding
    private val viewModel: CartModel by viewModels()
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProductdetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val title =intent.getStringExtra("product_title")
        val id =intent.getStringExtra("product_id")
        val price=intent.getStringExtra("product_price")
        val des= intent.getStringExtra("product_des")
        val imgLink=intent.getStringExtra("product_img")
        binding.tvTitle.text=title
        binding.tvPrice.text=price
        binding.tvDesdetail.text=des
        Glide.with(this@Productdetail).load(imgLink)
            .placeholder(R.drawable.outline_download_for_offline_24)
            .error(R.drawable.outline_hide_image_24)
            .into(binding.imgProduct)
        binding.toolbar.setOnClickListener {

        }
        binding.btnAddToCart.setOnClickListener {
            addToCart(id.toString(),title.toString(),des.toString(),price.toString().toDouble(),imgLink.toString())
        }
        binding.btnBuyNow.setOnClickListener {
            val intent1= Intent(this@Productdetail, BuyActivity::class.java)
            intent1.putExtra("productdetail_id",id)
            intent1.putExtra("productdetail_title",title)
            intent1.putExtra("productdetail_price",price)
            intent1.putExtra("productdetail_des",des)
            intent1.putExtra("productdetail_image",imgLink)
            startActivity(intent1)
        }
    }

    private fun addToCart(productId: String,title: String,des: String,price: Double,imgLink: String) {
        val userId= UserSession.currentUser?.uid.toString()
        val cart= CartItem(productId, title, price, imgLink, des)
        viewModel.addToCart(userId, cart,
            onSucess = { Toast.makeText(this@Productdetail
                ,"Thêm giỏ hàng thành công", Toast.LENGTH_SHORT).show()},
            onError = {err->
                Toast.makeText(this@Productdetail
                ,err, Toast.LENGTH_SHORT).show()})
    }

}