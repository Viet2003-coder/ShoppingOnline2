package com.example.shoppingonline.ui.product.adress

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shoppingonline.databinding.ActivityChooseAddressBinding
import com.example.shoppingonline.ui.product.Auth.AuthViewModel
import com.example.shoppingonline.ui.product.Auth.Login

class ChooseAddress : AppCompatActivity() {
    private lateinit var binding: ActivityChooseAddressBinding
    private val addressViewModel: AddressViewModel by viewModels()
    private val auThModel: AuthViewModel by viewModels()
    private lateinit var adpter: AddressAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChooseAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auThModel.loadUser()

        binding.btnAddAddress.setOnClickListener {
            startActivity(Intent(this@ChooseAddress, AddAddress::class.java))
        }

        auThModel.user.observe(this@ChooseAddress){user ->
            if (user==null){
                startActivity(Intent(this, Login::class.java))
                return@observe
            }
            val userId= user.uid
            adpter= AddressAdapter{addressItem ->
                val data= Intent().apply {
                    putExtra("addressId",addressItem.addressId)
                    putExtra("fullName",addressItem.fullName)
                    putExtra("phone",addressItem.phone)
                    putExtra("fullAddress",addressItem.fullAdress)
                    putExtra("lat",addressItem.latitude)
                    putExtra("lng",addressItem.longitude)
                    putExtra("note",addressItem.note)
                }
                setResult(RESULT_OK, data)
                finish()
            }
            binding.rvAddress.layoutManager= GridLayoutManager(this@ChooseAddress,1)
            binding.rvAddress.adapter=adpter
            addressViewModel.addressItems.observe(this){list->
                adpter.submitData(list)
            }
            addressViewModel.loadAddress(userId)
        }
    }

    override fun onResume() {
        super.onResume()
        auThModel.user.observe(this){user ->
            if (user==null){
                startActivity(Intent(this, Login::class.java))
                return@observe
            }
            addressViewModel.addressItems.observe(this){list->
                adpter.submitData(list)
            }
            addressViewModel.loadAddress(user.uid)
        }
    }
}