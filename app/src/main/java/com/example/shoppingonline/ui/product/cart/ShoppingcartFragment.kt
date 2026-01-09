package com.example.shoppingonline.ui.product.cart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingonline.ui.product.oder.BuyActivity
import com.example.shoppingonline.R
import com.example.shoppingonline.UserSession
import androidx.fragment.app.activityViewModels

class ShoppingcartFragment : Fragment() {
    private val cartModel : CartModel by activityViewModels()
    private lateinit var  adapter : CartAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_shoppingcart, container, false)
        val rcv=view.findViewById<RecyclerView>(R.id.rvCart)
        val toolbar=view.findViewById<Toolbar>(R.id.toolbarCart)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Giỏ hàng"
        }
        val userId= UserSession.currentUser?.uid.toString()
        adapter= CartAdapter { cartItem ->
            val intent = Intent(requireContext(), BuyActivity::class.java)
            intent.putExtra("product_id", cartItem.productId)
            intent.putExtra("product_title", cartItem.title)
            intent.putExtra("product_image", cartItem.thumbnail)
            intent.putExtra("product_des", cartItem.description)
            intent.putExtra("product_price", cartItem.price.toString())
            startActivity(intent)
        }
        rcv.layoutManager= GridLayoutManager(requireContext(), 1)
        rcv.adapter=adapter
        cartModel.carts.observe(viewLifecycleOwner){
            adapter.submitData(it)
        }
        cartModel.loadCart(userId)
        return view
    }

    override fun onResume() {
        super.onResume()
        val userId= UserSession.currentUser?.uid.toString()
        cartModel.carts.observe(viewLifecycleOwner){
            adapter.submitData(it)
        }
        cartModel.loadCart(userId)
    }
}