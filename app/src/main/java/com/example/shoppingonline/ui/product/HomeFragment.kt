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
import com.example.shoppingonline.ui.product.Productdetail
import com.example.shoppingonline.R


class HomeFragment : Fragment() {
    val homeModel : ProductModel by viewModels()
    private lateinit var adapter: ProductAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView=view.findViewById<RecyclerView>(R.id.recyclerBooks)
        adapter= ProductAdapter { product ->
            val intent = Intent(requireContext(), Productdetail::class.java)
            intent.putExtra("product_id", product.id.toString())
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
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                if (dy<=0) return//chỉ load khi kéo xuống
                val layoutManager = rv.layoutManager as GridLayoutManager
                val totalItem = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                // Khi còn 4 item nữa là tới đáy → load tiếp
                if (!homeModel.isLoading &&
                    lastVisible >= totalItem - 4
                ) {
                    homeModel.loadProducts()
                }
            }
        })
        return view
    }
}