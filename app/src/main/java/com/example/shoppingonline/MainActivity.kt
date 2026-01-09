package com.example.shoppingonline

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewPager2=findViewById<ViewPager2>(R.id.vpg2)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        viewPager2.adapter= ViewPager2Adapter(this)
        TabLayoutMediator(tabLayout,viewPager2){tab,position->
            tab.text=when(position){
                0->"Home"
                1->"Shopping Carts"
                2->"Oders"
                else -> "Profile"
            }
        }.attach()
    }
}