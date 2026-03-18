package com.example.shoppingonline.ui.product.oder

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.shoppingonline.R
import com.example.shoppingonline.databinding.ActivityOderDetailBinding
import com.example.shoppingonline.ui.product.Auth.AuthViewModel
import java.text.SimpleDateFormat
import kotlin.math.ceil

class OderDetail : AppCompatActivity() {
    private lateinit var binding: ActivityOderDetailBinding
    private val oderViewModel: OderViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val oderId = intent.getStringExtra("oderId").toString()
        binding.tvOrderId.text = oderId.takeLast(6)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        authViewModel.user.observe(this) { user ->
            if (user == null) {
                return@observe
            }
            oderViewModel.getOderDetail(this, user.uid, oderId)
        }
        oderViewModel.oder.observe(this) { oder ->
            if (oder==null) return@observe
            binding.tvReceiverName.text = oder.shippingAddress.fullName
            binding.tvReceiverPhone.text = oder.shippingAddress.phone
            binding.tvStatus.text = oder.status.toString()
            binding.tvProductPrice.text = oder.item.price.toString()
            binding.tvReceiverAddress.text = oder.shippingAddress.fullAdress
            binding.tvProductName.text = oder.item.title
            binding.tvProductQuantity.text = "Số lượng: ${oder.item.stock}"
            Glide.with(this).load(oder.item.thumbnail)
                .placeholder(R.drawable.outline_download_for_offline_24)
                .error(R.drawable.outline_hide_image_24)
                .into(binding.imgProduct)
            val subTotal = oder.item.price*oder.item.stock
            binding.tvSubTotal.text="${subTotal}$"
            val shippingFee= ceil(oder.shippingFee)
            binding.tvTotalAmount.text="${ceil(oder.totalPrice)}$"
            binding.tvShippingFee.text="${shippingFee}$"
            val formatDate=formatLongToDate(oder.createdAt)
            binding.tvOrderDate.text=formatDate
        }
        authViewModel.loadUser()
    }
    @SuppressLint("SimpleDateFormat")
    fun formatLongToDate(time: Long): String {
        val date = java.util.Date(time)
        val format= SimpleDateFormat("dd/MM/yyyy")
        return format.format(date)
    }
}