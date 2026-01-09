package com.example.shoppingonline.ui.product

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingonline.ui.product.cart.Productdetail
import com.example.shoppingonline.R


class HomeFragment : Fragment() {
    val homeModel : ProductModel by viewModels()
    private lateinit var adapter: ProductAdapter
    private lateinit var adapter2: ProductAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView=view.findViewById<RecyclerView>(R.id.recyclerBooks)
        val rcvTenPro=view.findViewById<RecyclerView>(R.id.recyclerNewArrivals)
        adapter= ProductAdapter { product ->
            val intent = Intent(requireContext(), Productdetail::class.java)
            intent.putExtra("product_id", product.id)
            intent.putExtra("product_title", product.title)
            intent.putExtra("product_des", product.description)
            intent.putExtra("product_price", product.price.toString())
            intent.putExtra("product_img", product.thumbnail)
            intent.putExtra("product_stock", product.stock)
            startActivity(intent)
        }
        adapter2= ProductAdapter { product ->
            val intent = Intent(requireContext(), Productdetail::class.java)
            intent.putExtra("product_id", product.id)
            intent.putExtra("product_title", product.title)
            intent.putExtra("product_des", product.description)
            intent.putExtra("product_price", product.price.toString())
            intent.putExtra("product_img", product.thumbnail)
            intent.putExtra("product_stock", product.stock)
            startActivity(intent)
        }
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        recyclerView.adapter = adapter
        homeModel.products.observe(viewLifecycleOwner){
            adapter.submitData(it)
        }
        homeModel.loadProducts()
//        rcvTenPro.adapter=adapter2
//        recyclerView.layoutManager= GridLayoutManager(requireContext(),1)
//        homeModel.tenproducts.observe(viewLifecycleOwner){
//            adapter2.submitData(
//                it
//            )
//        }
//        homeModel.loadTenProduct()
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)

                val layoutManager = rv.layoutManager as GridLayoutManager
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                if (lastVisible == adapter.itemCount - 1) {
                    homeModel.loadProducts() // load trang tiếp
                }
            }
        })
        return view
    }
}