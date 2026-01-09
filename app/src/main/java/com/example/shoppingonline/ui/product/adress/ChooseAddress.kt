package com.example.shoppingonline.ui.product.adress

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shoppingonline.UserSession
import com.example.shoppingonline.databinding.ActivityChooseAddressBinding

class ChooseAddress : AppCompatActivity() {
    private lateinit var binding: ActivityChooseAddressBinding
    private val addressViewModel: AddressModel by viewModels()
    private lateinit var adpter: AddressAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChooseAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnAddAddress.setOnClickListener {
            startActivity(Intent(this@ChooseAddress, AddAddress::class.java))
        }
        binding.rvAddress.layoutManager= GridLayoutManager(this@ChooseAddress,1)
        val userId= UserSession.currentUser?.uid.toString()
        adpter= AddressAdapter()
        binding.rvAddress.adapter=adpter
        addressViewModel.addressItems.observe(this){list->
            adpter.submitData(list)
        }
        addressViewModel.loadAddress(userId)
    }

    override fun onResume() {
        super.onResume()
        val userId= UserSession.currentUser?.uid.toString()
        addressViewModel.addressItems.observe(this){list->
            adpter.submitData(list)
        }
        addressViewModel.loadAddress(userId)
    }
}