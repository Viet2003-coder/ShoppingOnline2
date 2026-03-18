package com.example.shoppingonline.ui.product

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingonline.R
import com.example.shoppingonline.data.model.Comment
import com.example.shoppingonline.ui.product.Comment.CommentAdapter
import com.example.shoppingonline.ui.product.Comment.CommentViewModel
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText


class HomeFragment : Fragment() {
    val homeModel: ProductViewModel by viewModels()
    private lateinit var adapter: ProductAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerBooks)
        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupFilter)
        val edtSearch = view.findViewById<TextInputEditText>(R.id.edtSearch)
        homeModel.categories.observe(viewLifecycleOwner) { categories ->
            chipGroup.removeAllViews()
            // Thêm một Chip "Tất cả" mặc định
            val allChip = createChip("Tất cả")
            allChip.isChecked = true // Mặc định chọn cái đầu tiên
            chipGroup.addView(allChip)
            categories.forEach { categoryName ->
                val chip = createChip(categoryName)
                chipGroup.addView(chip)
            }
        }
        //hiển thị sản phẩm
        adapter = ProductAdapter { product ->
            val intent = Intent(requireContext(), Productdetail::class.java)
            intent.putExtra("product_id", product.id.toString())
            intent.putExtra("product_title", product.title)
            intent.putExtra("product_des", product.description)
            intent.putExtra("product_price", product.price.toString())
            intent.putExtra("product_img", product.thumbnail)
            intent.putExtra("product_stock", product.stock)
            startActivity(intent)
        }
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter
        homeModel.products.observe(viewLifecycleOwner) {
            adapter.submitData(it)
        }

        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }
            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val query = s.toString().trim()
                homeModel.setSearchQuery(query)
            }
        })
        homeModel.loadProducts()
        homeModel.getCategories()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                if (dy <= 0) return//chỉ load khi kéo xuống
                val layoutManager = rv.layoutManager as GridLayoutManager
                val totalItem = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                // Khi còn 4 item nữa là tới đáy → load tiếp
                if (!homeModel.isLoading && totalItem > 0 &&
                    lastVisible >= totalItem - 4
                ) {
                    homeModel.loadProducts()
                }
            }
        })
        return view

    }

    private fun createChip(name: String): com.google.android.material.chip.Chip {
        val chip = com.google.android.material.chip.Chip(requireContext())
        chip.text = name
        chip.isCheckable = true
        chip.setChipBackgroundColorResource(R.color.white) // Màu nền (tùy chỉnh theo theme của bạn)
        // Xử lý sự kiện khi nhấn vào Chip để lọc
        chip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                homeModel.filterByCategory(name)
            }
        }
        return chip
    }
}