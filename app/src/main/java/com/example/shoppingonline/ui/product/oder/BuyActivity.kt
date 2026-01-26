package com.example.shoppingonline.ui.product.oder

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.shoppingonline.CheckOnline
import com.example.shoppingonline.Model.AddressItem
import com.example.shoppingonline.Model.Oder
import com.example.shoppingonline.Model.OderItem
import com.example.shoppingonline.Model.Product
import com.example.shoppingonline.Model.User
import com.example.shoppingonline.UserSession
import com.example.shoppingonline.data.model.OderStatus
import com.example.shoppingonline.databinding.ActivityBuyBinding
import com.example.shoppingonline.ui.product.Auth.AuthViewModel
import com.example.shoppingonline.ui.product.ProductModel
import com.example.shoppingonline.ui.product.adress.ChooseAddress
import java.util.UUID
import kotlin.math.*

class BuyActivity : AppCompatActivity() {
    val currentLat=20.993776
    val currentLng=105.811417
    var spaceShip=0.0
    var priceShiping=0.0
    var product_price=0.0
    var stockProduct=0
    var product_title=""
    var product_thumnail=""
    var stock=1
    private val productViewModel : ProductModel by viewModels()
    private val oderViewModel : OderModel by viewModels()
    private val authViewModel : AuthViewModel by viewModels()
    var totalPrice=1.0
    var fullName=""
    var phone=""
    var fullAddress=""
    var lat= 0.0
    var lng=0.0
    var note=""
    var addressId=""
    var currentUser: User?=null
    private lateinit var binding: ActivityBuyBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBuyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarCheckout.setNavigationOnClickListener {
            finish()
        }
        val product_id=intent.getStringExtra("product_id").toString()
        stock=binding.tvQuantity.text.toString().toInt()
        authViewModel.loadUser()
        productViewModel.getProduct(product_id)
        productViewModel.product.observe(this){product ->
            product_price=product.price
            product_title= product.title
            product_thumnail=product.thumbnail
            stockProduct=product.stock
            binding.tvProductName.text=product_title
            binding.tvProductPrice.text=product_price.toString()
            Glide.with(this@BuyActivity).load(product.thumbnail).into(binding.imgProduct)
            totalPrice= stock * product_price + priceShiping
            binding.tvTotalPrice.text = ceil(totalPrice).toString()
            updatePrice()
        }
        authViewModel.user.observe(this){user ->
            binding.tvReceiver.text =
                if (fullName.isNotBlank()) "$fullName | $phone" else "${user.name} | ${user.phone}"
            binding.btnConfirmOrder.setOnClickListener {
                val newStock = stockProduct - stock
                if (stock > stockProduct) {
                    Toast.makeText(this, "Số lượng vượt quá tồn kho", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (phone.isEmpty()){
                    Toast.makeText(this, "Vui lòng chọn lại địa chỉ", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val oderItem= OderItem(product_id,product_title,product_price,product_thumnail,"",stock)
                val addressItem= AddressItem(addressId,fullName,phone,fullAddress,"","","",note,lat,lng)
                productViewModel.updateStock(product_id,newStock)
                productViewModel.syncStockToFireBase(product_id,newStock)
                val order= Oder(user.uid,UUID.randomUUID().toString(),oderItem,addressItem,totalPrice,
                    OderStatus.PENDING)
                oderViewModel.addOder(this@BuyActivity, order)
            }
            oderViewModel._message.observe(this){message->
                if (message=="Done"){
                    Toast.makeText(this@BuyActivity,"Đặt hàng thành công", Toast.LENGTH_SHORT).show()
                    stockProduct -=stock
                    if (stockProduct <=0) {
                        disableBtn()
                    }
                } else{
                    Toast.makeText(this@BuyActivity,"Lỗi ${message},vui lòng thử mua sau!!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        spaceShip=distanceInKm(lat,lng,currentLat,currentLng)
        priceShipTotal()
        binding.tvAddress.text=fullAddress
        binding.btnChangeAddress.setOnClickListener {
            if (!CheckOnline.isOnline(this)){
                Toast.makeText(this,"Vui lòng kết nối mạng để chọn địa chỉ!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            chooseAddressLauncher.launch(
                Intent(this, ChooseAddress::class.java)
            )
        }
        binding.btnMinus.setOnClickListener {
            if (stock<=1){
                Toast.makeText(this@BuyActivity,"Số lượng không thấp hơn được nữa", Toast.LENGTH_SHORT).show()
                binding.btnMinus.isEnabled=false
                return@setOnClickListener
            }
            else{
                stock-=1
            }
            updatePrice()
        }
        binding.btnPlus.setOnClickListener {
            if (stock>=stockProduct){
                Toast.makeText(this@BuyActivity,"Số lượng vượt quá", Toast.LENGTH_SHORT).show()
                binding.btnPlus.isEnabled=false
                return@setOnClickListener
            }
                stock+=1
            updatePrice()
        }

    }
    private val chooseAddressLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val data = result.data!!

                fullName = data.getStringExtra("fullName") ?: ""
                phone = data.getStringExtra("phone") ?: ""
                fullAddress = data.getStringExtra("fullAddress") ?: ""
                lat = data.getDoubleExtra("lat", 0.0)
                lng = data.getDoubleExtra("lng", 0.0)
                note = data.getStringExtra("note") ?: ""
                addressId = data.getStringExtra("addressId") ?: ""
                binding.tvReceiver.text = "$fullName | $phone"
                binding.tvAddress.text = fullAddress

                spaceShip = distanceInKm(lat, lng, currentLat, currentLng)
                priceShipTotal()
                updatePrice()
            }
        }
    private fun priceShipTotal() {
        if (spaceShip<=5){
            priceShiping=spaceShip*5
        }
        if (spaceShip>40){
            priceShiping=35.0
        }
        if (5<spaceShip&&spaceShip<=40){
            priceShiping=spaceShip
        }
        binding.tvShipPrice.text=ceil(priceShiping).toString()
    }

    fun distanceInKm(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val R = 6371.0 // Bán kính trái đất (km)
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2.0) +
                cos(Math.toRadians(lat1)) *
                cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2.0)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }
    private fun updatePrice() {
        binding.tvQuantity.text = stock.toString()
        totalPrice = stock * product_price + priceShiping
        binding.tvTotalPrice.text = ceil(totalPrice).toString()
        binding.btnPlus.isEnabled = stock < stockProduct
        binding.btnMinus.isEnabled = stock > 1
    }
    private fun disableBtn(){
            binding.btnPlus.isEnabled=false
            binding.btnMinus.isEnabled=false
            binding.tvQuantity.text="0"
            Toast.makeText(this, "Sản phẩm này hện đang hết hàng", Toast.LENGTH_SHORT).show()
            binding.btnConfirmOrder.isEnabled=false
            return
    }

}