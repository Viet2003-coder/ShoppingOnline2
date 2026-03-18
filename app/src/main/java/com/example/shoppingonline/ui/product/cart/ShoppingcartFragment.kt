package com.example.shoppingonline.ui.product.cart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingonline.ui.product.oder.BuyActivity
import com.example.shoppingonline.R
import androidx.fragment.app.activityViewModels
import com.example.shoppingonline.ui.product.Auth.AuthViewModel
import com.example.shoppingonline.ui.product.Auth.Login

class ShoppingcartFragment : Fragment() {
    private val cartViewModel : CartViewModel by activityViewModels()
    private val authViewModel : AuthViewModel by activityViewModels()
    private lateinit var  adapter : CartAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_shoppingcart, container, false)
        val rcv=view.findViewById<RecyclerView>(R.id.rvCart)
        val toolbar=view.findViewById<Toolbar>(R.id.toolbarCart)
        val layoutEmptyCart=view.findViewById<LinearLayout>(R.id.layoutEmptyCart)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
        }
        authViewModel.loadUser()
        adapter = CartAdapter { cartItem ->
            val intent = Intent(requireContext(), BuyActivity::class.java)
            intent.putExtra("product_id", cartItem.productId)
            startActivity(intent)
        }

        rcv.layoutManager = GridLayoutManager(requireContext(), 1)
        rcv.adapter = adapter
        authViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user == null) {
                startActivity(Intent(requireContext(), Login::class.java))
                requireActivity().finish()
                return@observe
            }

            val userId = user.uid
            cartViewModel.loadCart(userId)
        }
        cartViewModel.carts.observe(viewLifecycleOwner) {
            if (it.isEmpty()){
                layoutEmptyCart.visibility=View.VISIBLE
                rcv.visibility=View.GONE
            }else{
                layoutEmptyCart.visibility=View.GONE
                rcv.visibility=View.VISIBLE
                adapter.submitData(it)
            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()
       authViewModel.loadUser()
    }
}