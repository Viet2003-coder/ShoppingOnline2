package com.example.shoppingonline

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        askNotificationPermission()
        val viewPager2=findViewById<ViewPager2>(R.id.vpg2)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        viewPager2.adapter= ViewPager2Adapter(this)
        TabLayoutMediator(tabLayout,viewPager2){tab,position->
            when(position){
                0->tab.setIcon(R.drawable.baseline_home_24)
                1->tab.setIcon(R.drawable.outline_add_shopping_cart_24)
                2->tab.setIcon(R.drawable.baseline_shopping_bag_24)
                else -> tab.setIcon(R.drawable.outline_person_24)
            }
        }.attach()
    }
    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }
}