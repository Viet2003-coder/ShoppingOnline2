package com.example.shoppingonline

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.shoppingonline.ui.product.Auth.InformationFragment
import com.example.shoppingonline.ui.product.HomeFragment
import com.example.shoppingonline.ui.product.cart.ShoppingcartFragment
import com.example.shoppingonline.ui.product.oder.OderFragment

class ViewPager2Adapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> HomeFragment()
            1-> ShoppingcartFragment()
            2-> OderFragment()
            else -> InformationFragment()
        }
    }

    override fun getItemCount(): Int =4
}