package com.example.shoppingonline.ui.product.adress

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.doAfterTextChanged
import com.example.shoppingonline.Model.AddressItem
import com.example.shoppingonline.R
import com.example.shoppingonline.UserSession
import com.example.shoppingonline.databinding.ActivityAddAddressBinding
import com.example.shoppingonline.respository.AddressRepository
import com.google.android.gms.location.LocationServices
import java.util.Locale
import java.util.UUID

class AddAddress : AppCompatActivity() {
    private lateinit var binding: ActivityAddAddressBinding
    private val addressViewModel: AddressModel by viewModels(){
        AddressModelFactory(AddressRepository())
    }
    private val formViewModel: AddressFormModel by viewModels()
    var province: String=""
    var district: String=""
    var ward: String=""
    var street: String=""
    var latitude: Double=0.0
    var longtitude: Double=0.0
    var fullAdress: String=""
    private var lat=0.0
    private var lng=0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnPickLocation.setOnClickListener {
            PickLocation()
        }
        binding.btnSaveAddress.setOnClickListener {
            saveAddress()
        }
        binding.edtFullName.doAfterTextChanged {
            formViewModel.name=it.toString()
        }
        binding.edtPhone.doAfterTextChanged {
            formViewModel.phone=it.toString()
        }
        binding.edtNote.doAfterTextChanged {
            formViewModel.note=it.toString()
        }
        binding.edtFullName.setText(formViewModel.name ?:"")
        binding.edtPhone.setText(formViewModel.phone ?:"")
        binding.edtNote.setText(formViewModel.note ?:"")
    }
    private val pickMapLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val lat = result.data?.getDoubleExtra("lat", 0.0)
                val lng = result.data?.getDoubleExtra("lng", 0.0)

                if (lat != null && lng != null) {
                    handleLatLng(lat, lng)
                }
            }
        }
    @SuppressLint("SetTextI18n")
    private fun saveAddress() {
        val fullName=binding.edtFullName.text.toString()
        val number=binding.edtPhone.text.toString()
        val note=binding.edtNote.text.toString()
        val userId= UserSession.currentUser?.uid.toString()
        if (fullName.isEmpty()){
            Toast.makeText(this,"Vui lòng điền tên bạn", Toast.LENGTH_SHORT).show()
            binding.edtFullName.requestFocus()
            return
        }
        if (number.isEmpty()){
            Toast.makeText(this,"Vui lòng điền số điện thoại", Toast.LENGTH_SHORT).show()
            binding.edtPhone.requestFocus()
            return
        }
//        binding.edtPhone.addTextChangedListener(object : TextWatcher {
//            private var isFormatting = false
//
//            override fun afterTextChanged(s: Editable?) {
//                if (isFormatting) return
//                isFormatting = true
//
//                val digits = s.toString().filter { it.isDigit() }
//
//                val formatted = when {
//                    digits.length <= 4 -> digits
//                    digits.length <= 7 -> "${digits.substring(0,4)} ${digits.substring(4)}"
//                    else -> "${digits.substring(0,4)} ${digits.substring(4,7)} ${digits.substring(7)}"
//                }
//
//                s?.replace(0, s.length, formatted)
//                isFormatting = false
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })

        if (fullAdress.isEmpty()){
            Toast.makeText(this,"Bạn chưa chọn địa chỉ", Toast.LENGTH_SHORT).show()
            binding.tvSelectedAddress.requestFocus()
            return
        }
        addressViewModel.addAdress(userId = userId, address = AddressItem(
            UUID.randomUUID().toString(),
            fullName,
            number,
            province,
            district,
            ward,
            street,
            note,
            latitude,
            longtitude,
            true
        )
        )

    }
    //dialog pick map
    @SuppressLint("UseKtx")
    private fun PickLocation() {
        val dialog= Dialog(this)
        dialog.setContentView(R.layout.chooselocation_dialog)
        dialog.window?.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        val currentLocation=dialog.findViewById<LinearLayout>(R.id.layoutCurrentLocation)
        val pickForMap=dialog.findViewById<LinearLayout>(R.id.layoutPickFromMap)
        currentLocation.setOnClickListener {
            dialog.dismiss()
            getLocationCurrent()
        }
        pickForMap.setOnClickListener {
            pickMapLauncher.launch(Intent(this, PickMapLocation::class.java))
            dialog.dismiss()
        }
        dialog.show()
    }
    //get currenlocation
    private fun getLocationCurrent() {
        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Chưa có quyền → xin quyền
            requestLocationPermission()
            return
        }
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener {location ->
            val geocoder = Geocoder(this, Locale("vi", "VN"))
            val addresses=geocoder.getFromLocation(location.latitude,location.longitude,1)
            if (!addresses.isNullOrEmpty()){
                val addr=addresses[0]
                province=addr.adminArea ?:""
                district = addr.subAdminArea ?: ""
                ward = addr.subLocality ?: ""
                street = addr.thoroughfare ?: ""
                latitude = location.latitude
                longtitude = location.longitude
                fullAdress=addr.getAddressLine(0).toString()
                binding.tvSelectedAddress.text =
                    addr.getAddressLine(0)
            }
        }

    }
    //xin cấp quyền truy cập vị trí
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            if (granted) {
                getLocationCurrent()
            }
        }
    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
    //convert map từ lng và lat
    private fun handleLatLng(lat: Double, lng: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lng, 1)

        if (!addresses.isNullOrEmpty()) {
            val addr = addresses[0]

            street = addr.thoroughfare ?: addr.getAddressLine(0)
            ward = addr.subLocality ?: ""
            district = addr.subAdminArea ?: ""
            province = addr.adminArea ?: ""

            latitude = lat
            longtitude = lng

            binding.tvSelectedAddress.text =
                addr.getAddressLine(0)
        }
    }

}