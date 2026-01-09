package com.example.shoppingonline.ui.product.adress

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppingonline.databinding.ActivityPickMapLocationBinding
import java.util.Locale

class PickMapLocation : AppCompatActivity() {
    var lat: Double=0.0
    var lng: Double=0.0
    private lateinit var binding: ActivityPickMapLocationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPickMapLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnConvert.setOnClickListener {
            val addressStr = binding.etAddress.text.toString().trim()
            if (addressStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show()
            } else {
                convertAddressToLatLng(addressStr)
                val data = Intent().apply {
                    putExtra("lat", lat)
                    putExtra("lng", lng)
                }

                setResult(Activity.RESULT_OK, data)
                finish()
            }
        }
    }
    private fun convertAddressToLatLng(addressStr: String) {
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocationName(addressStr, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                lat = address.latitude
                lng = address.longitude

            } else {
                Toast.makeText(this, "Không tìm thấy địa chỉ", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}